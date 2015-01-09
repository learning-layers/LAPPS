/**
 * @typedef oidcConfig
 * @type {Object}
 * @property {string} server The url of the oidc server.
 * @property {string} clientId The id of the client web page.
 * @property {string} scope What information to retrieve from the server about
 *           the user.
 */

/**
 * @typedef platform
 * @type {Object}
 * @property {number} id The id of the platform. Should be the same as on the
 *           backend side.
 * @property {string} name The displayed name of the platform.
 * @property {string} icon The font-awesome icon to disply for this platform.
 * @property {string} agentRegEx The regular expresion to match in the user
 *           agent string for OS identification.
 * @property {boolean} isMobile True if platform is for mobile devices
 */
