package soya.framework.commons.cli;

import java.io.Serializable;
import java.util.*;

public final class Command implements Serializable {
    private final String command;
    private final Map<String, DefaultOption> options;
    private final Map<String, DefaultOption> longOptions;

    private Command(String command, Set<DefaultOption> options) {
        this.command = command;

        Map<String, DefaultOption> opts = new LinkedHashMap<>();
        Map<String, DefaultOption> longOpts = new LinkedHashMap<>();
        if (options != null) {
            options.forEach(o -> {
                if (o.option != null) {
                    opts.put(o.option, o);
                }

                if (o.longOption != null) {
                    longOpts.put(o.longOption, o);
                }
            });
        }

        this.options = Collections.unmodifiableMap(opts);
        this.longOptions = Collections.unmodifiableMap(longOpts);
    }

    public String getCommand() {
        return command;
    }

    public String[] getOptions() {
        return options.keySet().toArray(new String[options.size()]);
    }

    public String[] getLongOptions() {
        return longOptions.keySet().toArray(new String[longOptions.size()]);
    }

    public Option getOption(String opt) {
        if (options.containsKey(opt)) {
            return options.get(opt);

        } else if (longOptions.containsKey(opt)) {
            return longOptions.get(opt);

        } else {
            return null;

        }
    }

    public CommandLine parse(String[] args) {
        DefaultCommandline cli = new DefaultCommandline(this);

        return cli;
    }

    public static CommandLine parse(String cli) {
        return null;
    }

    public static CommandLine parse(String cli, CommandLineParser parser) {
        return null;
    }

    public static final Builder builder(String command) {
        return new Builder(command);
    }

    public static final OptionBuilder optionBuilder(String option) {
        return new OptionBuilder(option);
    }

    public static final class Builder {
        private String command;
        private Set<DefaultOption> options = new LinkedHashSet<>();

        private Builder(String command) {
            this.command = command;
        }

        public Builder command(String command) {
            this.command = command;
            return this;
        }

        public Builder option(OptionBuilder optionBuilder) {
            options.add(optionBuilder.create());
            return this;
        }

        public Command create() {
            if (command == null || command.trim().length() == 0) {
                throw new IllegalStateException("command is required");
            }

            return new Command(command, options);
        }
    }

    public static final class DefaultOption implements Option, Serializable {
        private final String option;

        private String description;

        private String longOption;

        private boolean required;

        private int minArgNum;

        private int maxArgNum;

        private String defaultValue;

        private DefaultOption(String option) {
            this.option = option;
        }

        @Override
        public String getOption() {
            return option;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public String getLongOption() {
            return longOption;
        }

        public boolean isRequired() {
            return required;
        }

        public int getMinArgNum() {
            return minArgNum;
        }

        public int getMaxArgNum() {
            return maxArgNum;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    public static class OptionBuilder {
        private final DefaultOption option;

        private OptionBuilder(String option) {
            this.option = new DefaultOption(option);
        }

        public OptionBuilder description(String description) {
            option.description = description;
            return this;
        }

        public OptionBuilder longOption(String longOption) {
            option.longOption = longOption;
            return this;
        }

        public OptionBuilder required() {
            option.required = true;
            return this;
        }

        public OptionBuilder minArgNum(int minArgNum) {
            option.minArgNum = minArgNum;
            return this;
        }

        public OptionBuilder maxArgNum(int maxArgNum) {
            option.maxArgNum = maxArgNum;
            return this;
        }

        private DefaultOption create() {
            return option;
        }


    }

    static class DefaultCommandline implements CommandLine {
        private final Command command;
        private Map<Option, Object> values = new HashMap<>();

        private DefaultCommandline(Command command) {
            this.command = command;
        }

        @Override
        public String getCommand() {
            return command.getCommand();
        }

        @Override
        public Option[] getOptions() {
            return command.options.values().toArray(new DefaultOption[command.options.size()]);
        }

        @Override
        public String getOptionValue(String option) {
            return null;
        }

        @Override
        public String[] getOptionValues(String option) {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(command.command);
            command.options.values().forEach(e -> {
                sb.append(" -").append(e.option);
                if(values.containsKey(e)) {
                    Object v = values.get(e);
                    if(v instanceof String) {
                        sb.append(" ").append(v);
                    } else if(v.getClass().isArray()) {

                    }
                }
            });
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Command cmd = Command.builder("echo")
                .option(Command
                        .optionBuilder("h")
                        .longOption("help")
                        .description("Help"))
                .create();

        System.out.println(cmd.parse(new String[]{}));

        System.exit(0);
    }
}
