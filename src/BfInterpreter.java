import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.Stack;

public class BfInterpreter {
  private static final int MEMORY_SIZE = 30000;

  public static void main(String[] args) {
    System.out.println("Welcome to the BfInterpreter!");

    if (args.length > 0) {

      String fileName = args[0];
      String code = "";
      try {
        code = java.nio.file.Files.readString(Paths.get(fileName));
      } catch (IOException e) {
        System.out.println("ERROR: Could not read file " + fileName);
        return;
      }

      interpretBrainf(code);
    } else {
      System.out.println("NO Brianf*** code provided.");
    }
  }

  private static void interpretBrainf(String code) {
    byte[] memory = new byte[MEMORY_SIZE];
    int dataPointer = 0;
    int programCounter = 0;
    Stack<Integer> loopStack = new Stack<>();
    Scanner scanner = new Scanner(System.in);

    while (programCounter < code.length()) {
      char command = code.charAt(programCounter);
      switch (command) {
        case '>':
          dataPointer = (dataPointer + 1) % MEMORY_SIZE;
          break;
        case '<':
          dataPointer = (dataPointer - 1 + MEMORY_SIZE) % MEMORY_SIZE;
          break;
        case '+':
          memory[dataPointer]++;
          break;
        case '-':
          memory[dataPointer]--;
          break;
        case '.':
          System.out.print((char) (memory[dataPointer] & 0xFF));
          break;
        case '[':
          if (memory[dataPointer] == 0) {
            int openBrackets = 1;
            while (openBrackets > 0) {
              programCounter++;
              if (code.charAt(programCounter) == '[')
                openBrackets++;
              if (code.charAt(programCounter) == ']')
                openBrackets--;
            }
          } else {
            loopStack.push(programCounter);
          }
          break;
        case ']':
          if (memory[dataPointer] != 0) {
            programCounter = loopStack.peek();
          } else {
            loopStack.pop();
          }
          break;
        default:
          break;
      }

      programCounter++;
    }
    scanner.close();
  }
}
