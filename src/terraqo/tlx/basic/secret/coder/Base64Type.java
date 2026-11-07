package pxf.toolkit.basic.secret.coder;

/**
 * Base64类型
 *
 * @author potatoxf
 * @date 2021/03/12
 */
public enum Base64Type {
  /** Base64 */
  STANDARD,
  /** Base64WithUrl */
  URL_SAFE,
  /** Base64WithMime */
  MIME_SAFE
}
