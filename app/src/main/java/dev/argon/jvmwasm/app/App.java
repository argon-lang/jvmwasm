/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dev.argon.jvmwasm.app;

import dev.argon.jvmwasm.engine.ModuleLinkException;
import dev.argon.jvmwasm.engine.validator.ValidationException;
import dev.argon.jvmwasm.format.ModuleFormatException;
import dev.argon.jvmwasm.format.text.ScriptCommand;
import dev.argon.jvmwasm.format.text.ScriptReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * App for executing wast scripts.
 * Requires the wasm spec interpreter to convert text format to binary.
 */
public class App {
	App() {}

	/**
	 * Execute wast scripts.
	 * @param args Command line arguments.
	 * @throws Throwable if an error occurs.
	 *
	 */
    public static void main(String[] args) throws Throwable {
		String scriptFile = args[0];

		String wasmExecutableStr = System.getenv("JVMWASM_WASM_PATH");
		Path wasmExecutable = Path.of(wasmExecutableStr);

		List<? extends ScriptCommand> commands;
		System.out.println("Reading module");
		try(var reader = Files.newBufferedReader(Path.of(scriptFile))) {
			commands = new ScriptReader(reader).readCommands();
		}

		System.out.println("Executing script");
		try(var interpreter = new ScriptInterpreter(wasmExecutable, new PrintWriter(System.out))) {
			interpreter.executeScript(commands);
		}
    }
}
