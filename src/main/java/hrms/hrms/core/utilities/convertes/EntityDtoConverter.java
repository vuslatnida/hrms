package hrms.hrms.core.utilities.convertes;

import org.modelmapper.ModelMapper;

public class EntityDtoConverter<F,T> {
    private ModelMapper modelMapper = new ModelMapper();
    private Class<T> toClass;

    public EntityDtoConverter(Class<T> toClass) {
        this.toClass = toClass;
    }

    public T convert(F from) {
        T to = this.modelMapper.map(from, this.toClass);
        return to;
    }
}