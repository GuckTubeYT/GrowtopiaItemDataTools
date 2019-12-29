package me.droreo002.tools.commands;

import lombok.Getter;
import me.droreo002.tools.ItemDataDecoder;
import me.droreo002.tools.commands.model.HelpCommand;
import me.droreo002.tools.commands.model.ItemDataCommand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    @Getter
    private final List<CommandExecutor> executors = new ArrayList<>();
    @Getter
    private ItemDataDecoder program;

    public CommandHandler(ItemDataDecoder program) {
        this.program = program;

        /*
        Register default
         */
        register(new HelpCommand());
        register(new ItemDataCommand(program));
    }

    /**
     * Get command from input
     *
     * @param input Console line input
     * @return The command
     */
    @Nullable
    public CommandExecutor getCommand(String input) {
        String[] args = input.split(" ");
        String cmdString = args[0].toLowerCase();
        return executors.stream().filter(c -> c.getCommand().toLowerCase().equals(cmdString)).findAny().orElse(null);
    }

    /**
     * Try to execute that command
     *
     * @return True if command is found, false otherwise
     */
    public boolean execute(String input) {
        CommandExecutor executor = getCommand(input);
        if (executor == null) return false;
        String[] actualArgs = input.split(" ");
        if (actualArgs.length > 1) {
            String[] args = new String[actualArgs.length - 1];
            System.arraycopy(actualArgs, 1, args, 0, args.length);

            executor.execute(program.getLogger(), args);
        } else {
            executor.execute(program.getLogger(), new String[]{});
        }
        return true;
    }

    /**
     * Register that command executor
     *
     * @param executor The executor to register
     */
    public void register(CommandExecutor executor) {
        if (isRegistered(executor.getCommand())) return;
        executors.add(executor);
        program.getLogger().info("Command " + executor.getCommand() + " has been registered!");
    }

    /**
     * Check if the command is registered
     *
     * @param command The command to check
     * @return true if registered, false otherwise
     */
    public boolean isRegistered(String command) {
        return executors.stream().anyMatch(c -> c.getCommand().toLowerCase().equals(command.toLowerCase()));
    }
}
