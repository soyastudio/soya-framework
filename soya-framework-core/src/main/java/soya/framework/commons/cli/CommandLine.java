package soya.framework.commons.cli;

import java.io.Serializable;

public interface CommandLine extends Serializable {
    String getCommand();

    Option[] getOptions();

    String getOptionValue(String option);

    String[] getOptionValues(String option);
}
