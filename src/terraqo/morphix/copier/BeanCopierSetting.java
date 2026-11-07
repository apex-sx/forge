package terraqo.morphix.copier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeanCopierSetting {
    
    private final Map<String, PropertyConverter<?, ?>> converters = new HashMap<>();
    
    public <S, T> BeanCopierSetting addConverter(String propertyName, PropertyConverter<S, T> converter) {
        Objects.requireNonNull(propertyName, "Property name cannot be null");
        Objects.requireNonNull(converter, "Converter cannot be null");
        converters.put(propertyName, converter);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public <S, T> PropertyConverter<S, T> getConverter(String propertyName) {
        return (PropertyConverter<S, T>) converters.get(propertyName);
    }
    
    public boolean hasConverter(String propertyName) {
        return converters.containsKey(propertyName);
    }
}
