package de.rwth.dbis.layers.lapps.resource;

/**
 * Helper class that contains a list of HTTP status codes that can be used by the ressource classes.
 */
public class HttpStatusCode {
  // Taken from: http://en.wikipedia.org/wiki/List_of_HTTP_status_codes

  // 1xx Informational
  public static final int CONTINUE = 100;
  public static final int SWITCHTING_PROTOCOLS = 101;
  public static final int PROCESSING = 102;

  // 2xx Success
  public static final int OK = 200;
  public static final int CREATED = 201;
  public static final int ACCEPTED = 202;
  public static final int NON_AUTHORATIVE_INOFORMATION = 203;
  public static final int NO_CONTENT = 204;
  public static final int RESET_CONTENT = 205;
  public static final int PARTIAL_CONTENT = 206;
  public static final int MULTI_STATUS = 207;
  public static final int ALREADY_REPORTED = 208;
  public static final int IM_USED = 226;

  // 3xx Redirection
  public static final int MULTIPLE_CHOICES = 300;
  public static final int MOVED_PERMANENTLY = 301;
  public static final int FOUND = 302;
  public static final int SEE_OTHER = 303;
  public static final int NOT_MODIFIED = 304;
  public static final int USE_PROXY = 305;
  public static final int SWITCH_PROXY = 306;
  public static final int TEMPORARY_REDIRECT = 307;
  public static final int PERMANENT_REDIRECT = 308;

  // 4xx Client Error
  public static final int BAD_REQUEST = 400;
  public static final int UNAUTHORIZED = 401;
  public static final int PAYMENT_REQUIRED = 402;
  public static final int INVALID_AUTHENFICATION = 401;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int METHOD_NOT_ALLOWED = 405;
  public static final int NOT_ACCEPTABLE = 406;
  public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
  public static final int REQUEST_TIMEOUT = 408;
  public static final int CONFLICT = 409;
  public static final int GONE = 410;
  public static final int LENGTH_REQUIRED = 411;
  public static final int PRECONDITION_FAILED = 412;
  public static final int REQUEST_ENTRY_TOO_LARGE = 413;
  public static final int REQUEST_URI_TOO_LONG = 414;
  public static final int UNSUPPORTED_MEDIA_TYPE = 415;
  public static final int REQUEST_RANGE_NOT_SATISFIABLE = 416;
  public static final int EXPECTATION_FAILED = 417;
  public static final int UNPROCESSABLE_ENTITY = 422;
  public static final int LOCKED = 423;
  public static final int FAILED_DEPENDENCY = 424;
  public static final int UPGRADE_REQUIRED = 426;
  public static final int PRECONDITION_REQUIRED = 428;
  public static final int TOO_MANY_REQUESTS = 429;
  public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

  // 5xx Server Error
  public static final int INTERNAL_SERVER_ERROR = 500;
  public static final int NOT_IMPLEMENTED = 501;
  public static final int SERVICE_UNAVAILABLE = 503;
  public static final int GATEWAY_TIMEOUT = 504;
  public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
  public static final int VARIANT_ALSO_NEGOTIATES = 506;
  public static final int INSUFFICIENT_STORAGE = 507;
  public static final int LOOP_DETECTED = 508;
  public static final int NOT_EXTENDED = 510;
  public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;

}
