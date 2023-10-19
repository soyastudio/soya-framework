package soya.framework.commons.cli;

import java.io.Serializable;

public interface Option extends Serializable {
    String getOption();

    String getLongOption();

    String getDescription();

    boolean isRequired();

    int getMinArgNum();

    int getMaxArgNum();

    String getDefaultValue();

}
