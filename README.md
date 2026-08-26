# Custom Language Interpreter & Parser (Java)

A lightweight programming language interpreter built in Java. The project converts raw source code into tokens, constructs an Abstract Syntax Tree (AST) using a top-down recursive descent parser, and executes the tree logic using a global dynamic memory space.

The language supports basic integer arithmetic, relational logic, console I/O, variable assignments, conditional branching (`if`/`else`), and multiple looping structures (`do while` and range-based `do` loops).

---

## Language Features & Syntax

* **Variables & Memory:** Dynamic, case-insensitive variables backed by a global memory map. Undeclared variables safely default to `0`.
* **Arithmetic Operations:** Standard precedence for addition (`+`), subtraction (`-`), multiplication (`*`), and division (`/`).
* **Relational Logic:** Relational operators delimited by dots:
  * `.lt.` (Less Than), `.le.` (Less Than or Equal)
  * `.gt.` (Greater Than), `.ge.` (Greater Than or Equal)
  * `.eq.` (Equal), `.ne.` (Not Equal)
* **Control Flow:**
  * **If Statements:** `if (<logical_expression>) then ... else ... end if`
  * **Do-While Loops:** `do while (<logical_expression>) ... end do`
  * **For-Style Loops:** `do <id> = <start_expr>, <end_expr> ... end do`
* **Console I/O:** `read <id>` (reads user integer input) and `print <id>` (outputs stored variable value).

---

## Architecture & Design

1. **Lexical Analyzer (`LexicalAnalyzer.java` & `Token.java`)**  
   Scans raw input, tracks precise line (`row`) and column (`col`) metadata for syntax error diagnostics, strips whitespace, and generates typed `Token` stream objects.

2. **Recursive Descent Parser (`Parser.java`)**  
   Parses tokens into an Abstract Syntax Tree (AST) enforcing formal grammar rules. Enforces mathematical operator precedence through production hierarchy: `ArithmeticExpression` $\rightarrow$ `Term` $\rightarrow$ `Factor`.

3. **AST Execution & Memory (`ParseTree.java`, `StatementNode.java`, `memoryLocation.java`)**  
   Uses object-oriented polymorphism (`evaluate()` / `execute()`) to traverse and evaluate nodes. Manages variable runtime states via a centralized `HashMap<String, Integer>`.

---

## Key Files Overview

| Component | Class / File | Description |
| :--- | :--- | :--- |
| **Lexer** | `LexicalAnalyzer.java` | Converts raw text into a stream of tokens with line/col metrics. |
| **Parser** | `Parser.java` | Top-down recursive descent parser building the AST. |
| **AST Root** | `ParseTree.java` | Wrapper for the root `ProgramNode` that triggers execution. |
| **Statements** | `StatementNode.java` | AST nodes for statements (`AssignmentNode`, `IfNode`, `DoNode`, `PrintNode`, etc.). |
| **Expressions** | `ArithmeticExpressionNode.java` | Base hierarchy for evaluation of mathematical terms and factors. |
| **Logic** | `LogicalExpressionNode.java` | Handles condition evaluation (`<`, `>`, `==`, etc.) between expressions. |
| **Memory** | `memoryLocation.java` | Static global memory store mapping variable identifiers to values. |
| **Driver** | `testParser.java` | Entry point script for loading, parsing, and executing test programs. |

---

## Running the Interpreter

### 1. Compilation
```bash
javac *.java
