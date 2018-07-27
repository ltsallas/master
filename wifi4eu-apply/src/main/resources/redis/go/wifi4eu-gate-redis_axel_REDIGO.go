 /* WIFI4EU-GATE
 *
 *
 *
 */

 package main

 import (
	 "database/sql"
	 "fmt"
	 "net/http"
	 "net/url"
	 "runtime"
	 "strconv"
	 "time"
	 "io"
	 "strings"
     "github.com/garyburd/redigo/redis"
 	 "github.com/labstack/echo"
 	 "github.com/labstack/echo/middleware"
 	 "github.com/labstack/gommon/log"
 	 "github.com/tomasen/realip"

	 _ "github.com/denisenkom/go-mssqldb"
 )
 
const MAX_POOL_RETRIES = 100
const HAPROXY_URI = "127.0.0.1:8000"
const APPLY_QUEUE = "wifi4eu-apply"
const COOKIE_NAME = "hasRequested"

//-- DEV ENVIRONMENT
const (
	dbUserName = "taqof2g1aamcdvf"
	dbPassword = "TFpKzD@8RkK1Be-/yV2D.QbC3lRzTL*H$EN"
	dbHost = "wifi4eu-dbserver.database.windows.net"
	dbPort = 1433
	dbName = "wifi4eudb-prod_01_2018-05-28T08-30Z"
)

type UserMunicipalityTuple struct {
    uId int64
    mId   int64
}

func initUsersMap() map[int64]string {
	fmt.Println("[I] Initializing users map")
	start := time.Now()

	const queryUsers = "SELECT u.id, u.csrf_token FROM users u INNER JOIN conditions_agreement ca ON u.id = ca.user_id WHERE u.csrf_token IS NOT NULL AND ca.status = 1"
	//const queryUsers = "SELECT u.id, u.csrf_token FROM users u INNER JOIN conditions_agreement ca ON u.id = ca.user_id WHERE u.csrf_token IS NOT NULL AND ca.status = 1 ORDER BY u.id OFFSET 0 ROWS FETCH NEXT 100 ROWS ONLY"
	
	var userMap = make(map[int64]string)
 
	u := &url.URL{
		 Scheme: "sqlserver",
		 User:   url.UserPassword(dbUserName, dbPassword),
		 Host:   fmt.Sprintf("%s:%d", dbHost, dbPort),
	}
	 
	 db, err := sql.Open("sqlserver", u.String()+"?database="+dbName)

	 if err != nil {
		 log.Fatal(err)
		 panic(err)
	 }
 
	 defer db.Close()
 
	 rows, err := db.Query(queryUsers)
 
	 if err != nil {
		 log.Fatal(err)
		 panic(err)
	 }
 
	 defer rows.Close()

	 for rows.Next() {
		var (
			uID   int64
			CSRFToken string
		)
		
		if err := rows.Scan(&uID, &CSRFToken); err != nil {
			log.Fatal(err)
			continue
		}
		
		fmt.Printf("%d is %s\n", uID, CSRFToken) //-- For testing purposes
		userMap[uID] = CSRFToken
	 }
 
	 fmt.Println("-- Loaded ", len(userMap), " items into usermap. Took: ", time.Now().Sub(start).String())
	 fmt.Println("[F] Finished users map initialization")
	 return userMap
 }

 func initRegistrationsMap() map[int64]UserMunicipalityTuple {
	fmt.Println("[I] Fetching registrations")
	start := time.Now()

	const queryCall = "SELECT ru._user as user_id, ru.registration as reg_id, m.id as mun_id FROM registration_users ru INNER JOIN registrations r ON r.id = ru.registration INNER JOIN municipalities m ON m.id = r.municipality WHERE r.allFiles_flag = 1"
	var regsMap = make(map[int64]UserMunicipalityTuple)
	
	u := &url.URL{
		Scheme: "sqlserver",
		User:   url.UserPassword(dbUserName, dbPassword),
		Host:   fmt.Sprintf("%s:%d", dbHost, dbPort),
   }
	
   db, err := sql.Open("sqlserver", u.String()+"?database="+dbName)

   if err != nil {
	   log.Fatal(err)
	   panic(err)
   }

   defer db.Close()

   rows, err := db.Query(queryCall)

   if err != nil {
	   log.Fatal(err)
	   panic(err)
   }

	defer rows.Close()

	for rows.Next() {
		var (
			uId		int64
			rId   	int64
			mId  	int64
		)

		if err := rows.Scan(&uId, &rId, &mId); err != nil {
			log.Fatal(err)
			continue
		}

		tupl := UserMunicipalityTuple{uId: uId, mId: mId}
		
		fmt.Printf("Added registration: (%d,%d) of user %d\n", rId, mId, uId) //-- For testing purposes
		regsMap[rId] = tupl
	}

	fmt.Println("-- Loaded ", len(regsMap), " items into registrationsMap. Took: ", time.Now().Sub(start).String())
	fmt.Println("[F] Fetching registrations")
	return regsMap
 }

func getCallOpen() (int64, time.Time, time.Time) {
	fmt.Println("[I] Fetching open call")
	start := time.Now()

	const queryCall = "SELECT id, start_date, end_date FROM calls WHERE cast(Datediff(s, '1970-01-01', GETUTCDATE()) AS bigint)*1000 BETWEEN start_date AND end_date"
	var (
		cId			int64
		callOpen   	int64
		callClose  	int64
	)

	u := &url.URL{
		 Scheme: "sqlserver",
		 User:   url.UserPassword(dbUserName, dbPassword),
		 Host:   fmt.Sprintf("%s:%d", dbHost, dbPort),
	}
	 
	db, err := sql.Open("sqlserver", u.String()+"?database="+dbName)

	if err != nil {
		log.Fatal(err)
		panic(err)
	}

	defer db.Close()
 
	rows, err := db.Query(queryCall)
 
	if err != nil {
		log.Fatal(err)
		panic(err)
	}
 
	defer rows.Close()

	for rows.Next() {

		if err := rows.Scan(&cId, &callOpen, &callClose); err != nil {
			log.Fatal(err)
			continue
		}
		
		fmt.Printf("-- Call: %d => [From: %s; To: %s]\n", cId, time.Unix(callOpen / 1000, 0).String(), time.Unix(callClose / 1000, 0).String())
	}
 
	if (callOpen == 0 || callClose == 0) {
		fmt.Println("-- No call open found. Took: ", time.Now().Sub(start).String())
	} else {
		fmt.Println("-- Loaded call. Took: ", time.Now().Sub(start).String())
	}
	fmt.Println("[F] Fetching open call")
	return cId, time.Unix(callOpen / 1000, 0), time.Unix(callClose / 1000, 0)
 }


  func newPool(addr string) *redis.Pool {
	 return &redis.Pool{
		 MaxIdle:     500,
		 MaxActive: 12000,
		 Wait: true,
		 IdleTimeout: 2 * time.Second,
		 Dial:        func() (redis.Conn, error) { return redis.Dial("tcp", addr) },
		 TestOnBorrow: func(c redis.Conn, t time.Time) error {
			 if time.Since(t) < time.Minute {
				 return nil
			 }
			 _, err := c.Do("PING")
			 return err
		 },
	 }
 }
 
 func main() {
	start := time.Now()

	//-- GLobal vars
	var totalRetries int = 0
	
	//-- Initialization
	var usersMap map[int64]string = initUsersMap()
	callId, callOpen, callClose := getCallOpen()
	var regsMap map[int64]UserMunicipalityTuple = initRegistrationsMap()
	//-- TODO Pending registrations

	if (usersMap == nil || len(usersMap) == 0) {
		log.Warn("No users were pre-fetched on server startup")
	}

	if (callOpen.IsZero() || callClose.IsZero()) { //-- FIXME not working
		log.Warn("No call is open")
	}

	if (regsMap == nil || len(regsMap) == 0) {
		log.Warn("No registrations were pre-fetched on server startup")
	}
	 
	//-- CONFIGURE GOLANG
	runtime.GOMAXPROCS(runtime.NumCPU())
 
	//-- Set up redis
    redisPool := newPool(HAPROXY_URI)
	defer redisPool.Close()
 
	//-- Set up HTTP listener
	e := echo.New()
 
	//-- Logging
	//e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())
 
    e.GET("/", func(c echo.Context) error {

        return c.String(http.StatusOK, "Server UP") //TODO return machine name

    })
 
    e.GET("/time", func(c echo.Context) error {

        return c.String(http.StatusOK, strconv.FormatInt(time.Now().Unix(), 10))

	})
 
	//-- FIXME: Will secure. Internal use only. 
    e.GET("/next-call", func(c echo.Context) error {
		var strs []string

		strs = append(strs, strconv.FormatInt(callId, 10))
		strs = append(strs, callOpen.String())
		strs = append(strs, callClose.String())

		return c.String(http.StatusOK, strings.Join(strs, " - "))
	})
	
	//-- FIXME: Will secure. Internal use only.
	e.GET("/is-call-open", func(c echo.Context) error {

        return c.String(http.StatusOK, strconv.FormatBool(time.Now().After(callOpen) && time.Now().Before(callClose)))

    })

    e.GET("/calls/:callId/apply/:r/:u/:m", func(c echo.Context) error {

		//-- 0. Read input parameters parameters
		cToken, err := strconv.ParseInt(c.Param("callId"), 10, 64)
        if (err != nil || cToken != callId) {
            return c.String(http.StatusBadRequest, "[1] Invalid arguments")  //-- No need to give more information 
		}
		
		rToken, err := strconv.ParseInt(c.Param("r"), 10, 64)
        if err != nil {
            return c.String(http.StatusBadRequest, "[2] Invalid arguments")
        }

        uToken, err := strconv.ParseInt(c.Param("u"), 10, 64)
        if err != nil {
            return c.String(http.StatusBadRequest, "[3] Invalid arguments")
        }

        mToken, err := strconv.ParseInt(c.Param("m"), 10, 64)
        if err != nil {
			return c.String(http.StatusBadRequest, "[4] Invalid arguments")
		}
		
		//-- 1. CHECK CALL IS OPEN
        now := time.Now()
        if ( !(now.After(callOpen) && now.Before(callClose)) ) {
            return c.String(http.StatusUnauthorized, "Call is not active")
		}
		
		//-- 2. Validate the userId, XSRF-Token and user agreement acceptance by finding the user on the in-memory userMap
		xsrfToken := c.Request().Header.Get("X-XSRF-TOKEN")
			//-- fmt.Println("-- XSRF: ", xsrfToken)
			//-- fmt.Println("-- MAP XSRF: ", usersMap[uToken])
		if (usersMap[uToken] != xsrfToken) {
			return c.String(http.StatusUnauthorized, "Unauthorized")
		}

		//-- 3. CHECK R,U,M TOKEN
		var tupl UserMunicipalityTuple = regsMap[rToken];
			//-- fmt.Printf("-- Municipality Received: %d. Found: %d \n", mToken, tupl.mId)
			//-- fmt.Printf("-- User Received: %d. Found: %d \n", uToken, tupl.uId)
		if(tupl.mId != mToken || tupl.uId != uToken) {
			return c.String(http.StatusBadRequest, "Application does not meet the requirements")
		}

        //-- 4. SAVE TO REDIS
        retries := 0

        retry:
        conn := redisPool.Get()
        defer conn.Close()

        resp, err := conn.Do("XADD", APPLY_QUEUE, "*",
            "r", rToken,
            "u", uToken,
            "m", mToken,
            "ip", realip.FromRequest(c.Request()),
            "time", now.String())

        if err != nil {
            if err == io.EOF {
                retries++
                totalRetries++
                log.Info("Retrying!!")
                if (retries < MAX_POOL_RETRIES) {
                    goto retry
                }
            }

            log.Error(err.Error())

            return c.String(http.StatusInternalServerError, "Error processing request")
        }

		//-- 4. All good. Set cookie and return OK
		cookie := new(http.Cookie)
		cookie.Name = COOKIE_NAME
		cookie.Value = "true"
		cookie.Expires = time.Now().Add(31 * 24 * time.Hour)
		c.SetCookie(cookie)	

        return c.String(http.StatusOK, resp.(string))
	})
	
	fmt.Println("\n\n-- Server startup time: ", time.Now().Sub(start).String())
	
	e.Logger.Fatal(e.Start(":80"))
 }
