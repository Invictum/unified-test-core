package com.github.invictum.dto.attribute.converter.converters;

import com.github.invictum.dto.attribute.converter.Converter;
import com.github.invictum.dto.attribute.converter.ConverterUtil;

import java.util.List;

public class ListConverter implements Converter {

    @Override
    public boolean isTypeCompatible(Object o) {
        return List.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String convert(Object o) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Object item : (List<Object>) o) {
            builder.append(ConverterUtil.convert(item)).append(", ");
        }
        builder = builder.delete(builder.toString().length() - 2, builder.toString().length());
        return builder.append("]").toString();
    }
}
