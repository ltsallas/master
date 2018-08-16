// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.

export const environment = {
    production: false,
    logoutUrl: "https://ecas.acceptance.ec.europa.eu/cas/logout",
    applyVoucherUrl: "https://wifi4eu-dev-queue.everincloud.com",
    homeURL : "wifi4eu.everisdigitalchannels.com/wifi4eu/#/home",
    currentEnvironment: "test"
};
