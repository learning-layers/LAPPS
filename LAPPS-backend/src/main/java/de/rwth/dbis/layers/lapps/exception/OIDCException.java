package de.rwth.dbis.layers.lapps.exception;

/**
 * Own Exception class for all Open ID connect related exceptions
 * 
 */
public class OIDCException extends Exception {

  private static final long serialVersionUID = -8501019086683476020L;

  public OIDCException() {}

  public OIDCException(String s) {
    super(s);
  }
}
