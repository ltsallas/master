package wifi4eu.wifi4eu.apply;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import wifi4eu.wifi4eu.apply.localEntity.LocalEntity;
import wifi4eu.wifi4eu.apply.localEntity.LocalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterCommitterTest {

    @MockBean
    private LocalRepository localEntityRepository;

    @Autowired
    private MasterCommitter masterCommitter;

    @Before
    public void setUp() {
    	Date dateLocalEntity = new Date();
    	LocalEntity localEntity1 = new LocalEntity(1, 1, 1254, dateLocalEntity);
    	LocalEntity localEntity2 = new LocalEntity(1, 1, 1254, dateLocalEntity);
    	LocalEntity localEntity3 = new LocalEntity(1, 1, 1254, dateLocalEntity);
    	
    	List<LocalEntity> localEntities = Arrays.asList(localEntity1, localEntity2, localEntity3);
    	
        Mockito.when(this.localEntityRepository.findAll()).thenReturn(localEntities);
    }
    
    /**
     * This is the simplest case. There must not be any errors.
     * 
     */
    @Test
    public void testCase1() throws Exception {
        
        this.masterCommitter.commit();
        
    }
	
}
