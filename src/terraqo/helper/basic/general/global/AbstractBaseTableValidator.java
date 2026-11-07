package potatoxf.helper.basic.general.global;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import potatoxf.helper.api.Tools;
import potatoxf.helper.basic.common.entity.AbstractBaseTable;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author potatoxf
 * @date 2022/7/13
 */
@Component
public class AbstractBaseTableValidator implements Validator {

  private final Pattern yearMonthDayPattern = Tools.getYearMonthDayPattern("-", "-", "-");
  /**
   * Can this {@link Validator} {@link #validate(Object, Errors) validate} instances of the supplied
   * {@code clazz}?
   *
   * <p>This method is <i>typically</i> implemented like so:
   *
   * <pre class="code">return Foo.class.isAssignableFrom(clazz);</pre>
   *
   * (Where {@code Foo} is the class (or superclass) of the actual object instance that is to be
   * {@link #validate(Object, Errors) validated}.)
   *
   * @param clazz the {@link Class} that this {@link Validator} is being asked if it can {@link
   *     #validate(Object, Errors) validate}
   * @return {@code true} if this {@link Validator} can indeed {@link #validate(Object, Errors)
   *     validate} instances of the supplied {@code clazz}
   */
  @Override
  public boolean supports(@Nonnull Class<?> clazz) {
    return AbstractBaseTable.class.isAssignableFrom(clazz);
  }

  /**
   * Validate the supplied {@code target} object, which must be of a {@link Class} for which the
   * {@link #supports(Class)} method typically has (or would) return {@code true}.
   *
   * <p>The supplied {@link Errors errors} instance can be used to report any resulting validation
   * errors.
   *
   * @param target the object that is to be validated
   * @param errors contextual state about the validation process
   * @see ValidationUtils
   */
  @Override
  public void validate(@Nonnull Object target, @Nonnull Errors errors) {
    AbstractBaseTable<?, ?> user = (AbstractBaseTable<?, ?>) target;

  }

  @InitBinder
  public void dataBinding(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // setLenient用于设置Calendar是否宽松解析字符串，如果为false，则严格解析；默认为true，宽松解析
    dateFormat.setLenient(false);
    CustomDateEditor customDateEditor = new CustomDateEditor(dateFormat, true);
    binder.registerCustomEditor(Date.class, "createdTimeStart", customDateEditor);
    binder.registerCustomEditor(Date.class, "createdTimeEnd", customDateEditor);
    binder.registerCustomEditor(Date.class, "updatedTimeStart", customDateEditor);
    binder.registerCustomEditor(Date.class, "updatedTimeEnd", customDateEditor);
  }
}
