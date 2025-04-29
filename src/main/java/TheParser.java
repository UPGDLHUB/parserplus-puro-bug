import java.util.Vector;
public class TheParser {
    
    private Vector<TheToken> tokens;
    private int currentToken;
    
    public TheParser(Vector<TheToken> tokens) {
        this.tokens = tokens;
        currentToken = 0;
    }
    
    public void run() {
        RULE_PROGRAM();
    }
    
    private void RULE_PROGRAM() {
        System.out.println("- RULE_PROGRAM");
        if (tokens.get(currentToken).getValue().equals("{")) {
            currentToken++;
            System.out.println("- {");
        } else {
            error(1);
        }
        RULE_BODY();
        if (tokens.get(currentToken).getValue().equals("}")) {
            currentToken++;
            System.out.println("- }");
        } else {
            error(2);
        }
    }
    
    public void RULE_BODY() {
        System.out.println("-- RULE_BODY");
        while (!tokens.get(currentToken).getValue().equals("}")) {
            RULE_STATEMENT();
        }
    }
    
    public void RULE_STATEMENT() {
        System.out.println("--- RULE_STATEMENT");
        // Check for if-statement
        if (tokens.get(currentToken).getValue().equals("if")) {
            RULE_IF_STATEMENT();
        } else {
            RULE_EXPRESSION();
            if (tokens.get(currentToken).getValue().equals(";")) {
                System.out.println("--- ;");
                currentToken++;
            } else {
                error(3);
            }
        }
    }
    
    public void RULE_IF_STATEMENT() {
        System.out.println("--- RULE_IF_STATEMENT");
        // Consume 'if'
        currentToken++;
        System.out.println("--- if");
        
        // Check for opening parenthesis
        if (tokens.get(currentToken).getValue().equals("(")) {
            currentToken++;
            System.out.println("--- (");
        } else {
            error(6);
        }
        
        // Parse condition
        RULE_EXPRESSION();
        
        // Check for closing parenthesis
        if (tokens.get(currentToken).getValue().equals(")")) {
            currentToken++;
            System.out.println("--- )");
        } else {
            error(7);
        }
        
        // Parse if body
        RULE_STATEMENT_BLOCK();
        
        // Check for optional else
        if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("else")) {
            currentToken++;
            System.out.println("--- else");
            RULE_STATEMENT_BLOCK();
        }
    }
    
    public void RULE_STATEMENT_BLOCK() {
        System.out.println("---- RULE_STATEMENT_BLOCK");
        if (tokens.get(currentToken).getValue().equals("{")) {
            currentToken++;
            System.out.println("---- {");
            
            // Parse statements inside block
            while (!tokens.get(currentToken).getValue().equals("}")) {
                RULE_STATEMENT();
            }
            
            currentToken++;
            System.out.println("---- }");
        } else {
            // Single statement
            RULE_STATEMENT();
        }
    }
    
    public void RULE_EXPRESSION() {
        System.out.println("--- RULE_EXPRESSION");
        RULE_X();
        while (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("|")) {
            currentToken++;
            System.out.println("--- |");
            RULE_X();
        }
    }
    
    public void RULE_X() {
        System.out.println("---- RULE_X");
        RULE_Y();
        while (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("&")) {
            currentToken++;
            System.out.println("---- &");  // Corregido de "|" a "&"
            RULE_Y();
        }
    }
    
    public void RULE_Y() {
        System.out.println("----- RULE_Y");
        if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("!")) {
            currentToken++;
            System.out.println("----- !");
        }
        RULE_R();
    }
    
    public void RULE_R() {
        System.out.println("------ RULE_R");
        RULE_E();
        while (currentToken < tokens.size() && 
              (tokens.get(currentToken).getValue().equals("<")
            || tokens.get(currentToken).getValue().equals(">")
            || tokens.get(currentToken).getValue().equals("==")
            || tokens.get(currentToken).getValue().equals("!="))
        ) {
            currentToken++;
            System.out.println("------ relational operator");
            RULE_E();
        }
    }
    
    public void RULE_E() {
        System.out.println("------- RULE_E");
        RULE_A();
        while (currentToken < tokens.size() && 
              (tokens.get(currentToken).getValue().equals("-")
            || tokens.get(currentToken).getValue().equals("+"))
        ) {
            currentToken++;
            System.out.println("------- + or -");
            RULE_A();
        }
        
    }
    
    public void RULE_A() {
        System.out.println("-------- RULE_A");
        RULE_B();
        while (currentToken < tokens.size() && 
              (tokens.get(currentToken).getValue().equals("/")
            || tokens.get(currentToken).getValue().equals("*"))
        ) {
            currentToken++;
            System.out.println("-------- * or /");
            RULE_B();
        }
        
    }
    
    public void RULE_B() {
        System.out.println("--------- RULE_B");
        if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("-")) {
            currentToken++;
            System.out.println("--------- -");
        }
        RULE_C();
    }
    
    public void RULE_C() {
        System.out.println("---------- RULE_C");
        if (currentToken < tokens.size()) {
            if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
                currentToken++;
                System.out.println("---------- IDENTIFIER");
            } else if (tokens.get(currentToken).getType().equals("INTEGER")) {
                currentToken++;
                System.out.println("---------- INTEGER");
            } else if (tokens.get(currentToken).getValue().equals("(")) {
                currentToken++;
                System.out.println("---------- (");
                RULE_EXPRESSION();
                if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
                    currentToken++;
                    System.out.println("---------- )");
                } else {
                    error(4);
                }
            } else {
                error(5);
            }
        } else {
            error(60);  // Token inesperado
        }
    }
    
    private void error(int error) {
        if (currentToken < tokens.size()) {
            System.out.println("Error " + error +
                " at line " + tokens.get(currentToken).getLine() + 
                ", token: " + tokens.get(currentToken).getValue());
        } else {
            System.out.println("Error " + error + " at end of file");
        }
        System.exit(1);
    }
}
