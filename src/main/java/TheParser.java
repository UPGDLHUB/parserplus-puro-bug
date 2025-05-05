import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Main class to run the lexer
 *
 * @author sebastianavilez
 * @author gabrieltorreszacarias
 */

public class TheParser {

	private Vector<TheToken> tokens;
	private int currentToken;
	private Map<String, Set<String>> firstSetMap;
	private Map<String, Set<String>> followSetMap;

	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
		initializeGrammarSets();
	}
	
	private void initializeGrammarSets() {
		firstSetMap = new HashMap<>();
		followSetMap = new HashMap<>();
		setupFirstSets();
		setupFollowSets();
	}

	private void setupFirstSets() {
		firstSetMap.put("PROGRAM", new HashSet<>(Arrays.asList("{", "class")));
		firstSetMap.put("METHODS", new HashSet<>(Arrays.asList("int", "float", "void", "char", "string", "boolean")));
		
		Set<String> parameterFirstSet = new HashSet<>(Arrays.asList("int", "float", "void", "char", "string", "boolean"));
		parameterFirstSet.add("");
		firstSetMap.put("PARAMS", parameterFirstSet);
		
		Set<String> bodyFirstSet = new HashSet<>();
		bodyFirstSet.addAll(firstSetMap.get("METHODS"));
		bodyFirstSet.add("IDENTIFIER");
		bodyFirstSet.addAll(Arrays.asList("return", "while", "if", "do", "for", "switch", "(", "!", "-", "LITERAL", "break", ""));
		firstSetMap.put("BODY", bodyFirstSet);
		
		firstSetMap.put("VARIABLE", new HashSet<>(firstSetMap.get("METHODS")));
		firstSetMap.put("ASSIGNMENT", new HashSet<>(Collections.singletonList("IDENTIFIER")));
		firstSetMap.put("CALL_METHOD", new HashSet<>(Collections.singletonList("IDENTIFIER")));
		firstSetMap.put("PARAM_VALUES", new HashSet<>(Arrays.asList("IDENTIFIER", "(", "!", "-", "LITERAL", "")));
		firstSetMap.put("RETURN", new HashSet<>(Collections.singletonList("return")));
		firstSetMap.put("WHILE", new HashSet<>(Collections.singletonList("while")));
		firstSetMap.put("IF", new HashSet<>(Collections.singletonList("if")));
		firstSetMap.put("DO_WHILE", new HashSet<>(Collections.singletonList("do")));
		firstSetMap.put("FOR", new HashSet<>(Collections.singletonList("for")));
		firstSetMap.put("SWITCH", new HashSet<>(Collections.singletonList("switch")));
		
		Set<String> statementBlockFirstSet = new HashSet<>();
		statementBlockFirstSet.add("{");
		statementBlockFirstSet.addAll(firstSetMap.get("METHODS"));
		statementBlockFirstSet.add("IDENTIFIER");
		statementBlockFirstSet.addAll(Arrays.asList("return", "while", "if", "do", "for", "switch", "(", "!", "-", "LITERAL"));
		firstSetMap.put("STATEMENT_BLOCK", statementBlockFirstSet);
		
		Set<String> expressionFirstSet = new HashSet<>(Arrays.asList("IDENTIFIER", "(", "!", "-", "LITERAL"));
		firstSetMap.put("EXPRESSION", expressionFirstSet);
		firstSetMap.put("X", new HashSet<>(expressionFirstSet));
		
		Set<String> logicalTermFirstSet = new HashSet<>(expressionFirstSet);
		logicalTermFirstSet.add("!");
		firstSetMap.put("Y", logicalTermFirstSet);
		
		firstSetMap.put("R", new HashSet<>(Arrays.asList("IDENTIFIER", "(", "-", "LITERAL")));
		firstSetMap.put("E", new HashSet<>(Arrays.asList("IDENTIFIER", "(", "-", "LITERAL")));
		firstSetMap.put("A", new HashSet<>(Arrays.asList("IDENTIFIER", "(", "-", "LITERAL")));
		firstSetMap.put("B", new HashSet<>(Arrays.asList("-", "IDENTIFIER", "(", "LITERAL")));
		firstSetMap.put("C", new HashSet<>(Arrays.asList("IDENTIFIER", "(", "LITERAL")));
		firstSetMap.put("TYPE", new HashSet<>(Arrays.asList("int", "float", "void", "char", "string", "boolean")));
	}
	
	private void setupFollowSets() {
		followSetMap.put("PROGRAM", new HashSet<>(Collections.singletonList("$")));
		followSetMap.put("METHODS", new HashSet<>(Arrays.asList("int", "float", "void", "char", "string", "boolean", "}")));
		followSetMap.put("PARAMS", new HashSet<>(Collections.singletonList(")")));
		followSetMap.put("BODY", new HashSet<>(Arrays.asList("}", "break", "case", "default")));
		followSetMap.put("VARIABLE", new HashSet<>(Collections.singletonList(";")));
		followSetMap.put("ASSIGNMENT", new HashSet<>(Collections.singletonList(";")));
		followSetMap.put("CALL_METHOD", new HashSet<>(Arrays.asList(";", "+", "-", "*", "/", ")", "<", ">", "==", "!=", "&&", "||", ",")));
		followSetMap.put("PARAM_VALUES", new HashSet<>(Collections.singletonList(")")));
		followSetMap.put("RETURN", new HashSet<>(Arrays.asList("}", "break", "case", "default")));
		followSetMap.put("WHILE", new HashSet<>(Arrays.asList("}", ";", "else", "break", "case", "default")));
		followSetMap.put("IF", new HashSet<>(Arrays.asList("}", ";", "else", "break", "case", "default")));
		followSetMap.put("DO_WHILE", new HashSet<>(Arrays.asList("}", ";", "else", "break", "case", "default")));
		followSetMap.put("FOR", new HashSet<>(Arrays.asList("}", ";", "else", "break", "case", "default")));
		followSetMap.put("SWITCH", new HashSet<>(Arrays.asList("}", ";", "else", "break", "case", "default")));
		followSetMap.put("STATEMENT_BLOCK", new HashSet<>(Arrays.asList("}", ";", "else", "while", "break", "case", "default")));
		followSetMap.put("EXPRESSION", new HashSet<>(Arrays.asList(";", ")", ",", ":")));
		followSetMap.put("X", new HashSet<>(Arrays.asList(";", ")", ",", ":")));
		followSetMap.put("Y", new HashSet<>(Arrays.asList("||", ";", ")", ",", ":")));
		followSetMap.put("R", new HashSet<>(Arrays.asList("&&", "||", ";", ")", ",", ":")));
		followSetMap.put("E", new HashSet<>(Arrays.asList("<", ">", "==", "!=", "&&", "||", ";", ")", ",", ":")));
		followSetMap.put("A", new HashSet<>(Arrays.asList("+", "-", "<", ">", "==", "!=", "&&", "||", ";", ")", ",", ":")));
		followSetMap.put("B", new HashSet<>(Arrays.asList("*", "/", "+", "-", "<", ">", "==", "!=", "&&", "||", ";", ")", ",", ":")));
		followSetMap.put("C", new HashSet<>(Arrays.asList("*", "/", "+", "-", "<", ">", "==", "!=", "&&", "||", ";", ")", ",", ":")));
		followSetMap.put("TYPE", new HashSet<>(Collections.singletonList("IDENTIFIER")));
	}

	private boolean isTokenInFirstSetOf(String rule) {
		if (currentToken >= tokens.size()) return false;
		
		String value = tokens.get(currentToken).getValue();
		String type = tokens.get(currentToken).getType();
		Set<String> firstSet = firstSetMap.get(rule);
		
		if (firstSet.contains(value) || firstSet.contains(type)) return true;
		
		// Manejo especial para literales
		if ((type.equals("INTEGER") || type.equals("FLOAT") || type.equals("CHAR") || 
			type.equals("STRING") || type.equals("HEXADECIMAL") || type.equals("BINARY")) && 
			firstSet.contains("LITERAL")) return true;
		
		return false;
	}
	
	private boolean isTokenInFollowSetOf(String rule) {
		if (currentToken >= tokens.size()) return followSetMap.get(rule).contains("$");
		String value = tokens.get(currentToken).getValue();
		String type = tokens.get(currentToken).getType();
		return followSetMap.get(rule).contains(value) || followSetMap.get(rule).contains(type);
	}

	private boolean skipUntilFirstOrFollow(String rule, int errorCode) {
		error(errorCode);
		while (currentToken < tokens.size()) {
			if (isTokenInFirstSetOf(rule)) return true;
			if (isTokenInFollowSetOf(rule)) return false;
			currentToken++;
		}
		return false;
	}

	public void run() {
		try {
			RULE_PROGRAM();
			if (currentToken != tokens.size()) {
				error(99); // Unexpected tokens at the end
				System.out.println("Recovery: Skipping trailing tokens after valid program");
			}
			System.out.println("Parsing completed with recovery.");
		} catch (Exception e) {
			System.out.println("Critical error during parsing: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("- {");
			RULE_BODY();
			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("- }");
			} else {
				error(1);
			}
		} else if (tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("class")) {
			currentToken++;
			System.out.println("-- class");

			if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			} else {
				error(2);
			}

			if (tokens.get(currentToken).getValue().equals("{")) {
				currentToken++;
				System.out.println("---- {");

				while (!tokens.get(currentToken).getValue().equals("}")) {
					if (isType()) {
						if (isMethodDeclaration()) {
							RULE_METHODS();
						} else {
							RULE_VARIABLE();
							if (tokens.get(currentToken).getValue().equals(";")) {
								currentToken++;
								System.out.println("---- ;");
							} else {
								error(3);
							}
						}
					} else {
						error(4);
					}
				}

				if (tokens.get(currentToken).getValue().equals("}")) {
					currentToken++;
					System.out.println("---- }");
				} else {
					error(5);
				}
			} else {
				error(6);
			}
		} else {
			error(7);
		}
	}

	private void RULE_METHODS() {
		System.out.println("----- RULE_METHODS");
		
		if (!isTokenInFirstSetOf("METHODS")) {
			boolean foundFirst = skipUntilFirstOrFollow("METHODS", 700);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping METHODS rule");
				return;
			}
		}
		
		RULE_TYPE();
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("----- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;
		} else {
			error(8);
			// Saltar hasta encontrar un "(" o algo en FOLLOW(METHODS)
			while (currentToken < tokens.size() && 
				!tokens.get(currentToken).getValue().equals("(") && 
				!isTokenInFollowSetOf("METHODS")) {
				currentToken++;
			}
			
			if (currentToken >= tokens.size() || !tokens.get(currentToken).getValue().equals("(")) {
				System.out.println("Recovered: Missing method name, skipping method declaration");
				return;
			}
			System.out.println("Recovered: Found opening parenthesis after missing method name");
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("----- (");
			RULE_PARAMS();
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("----- )");
			} else {
				error(9);
				// Saltar hasta encontrar un "{" o algo en FOLLOW(METHODS)
				while (currentToken < tokens.size() && 
					!tokens.get(currentToken).getValue().equals("{") && 
					!isTokenInFollowSetOf("METHODS")) {
					currentToken++;
				}
				
				if (currentToken >= tokens.size() || !tokens.get(currentToken).getValue().equals("{")) {
					System.out.println("Recovered: Missing closing parenthesis, skipping method declaration");
					return;
				}
				System.out.println("Recovered: Found opening brace after missing closing parenthesis");
			}
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("{")) {
				currentToken++;
				System.out.println("----- {");
				RULE_BODY();
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("}")) {
					currentToken++;
					System.out.println("----- }");
				} else {
					error(10);
					// Saltar hasta algo en FOLLOW(METHODS)
					while (currentToken < tokens.size() && !isTokenInFollowSetOf("METHODS")) {
						currentToken++;
					}
					System.out.println("Recovered: Missing closing brace, skipping to next method or class end");
				}
			} else {
				error(11);
				// Saltar hasta algo en FOLLOW(METHODS)
				while (currentToken < tokens.size() && !isTokenInFollowSetOf("METHODS")) {
					currentToken++;
				}
				System.out.println("Recovered: Missing method body, skipping to next method or class end");
			}
		} else {
			error(12);
			// Saltar hasta algo en FOLLOW(METHODS)
			while (currentToken < tokens.size() && !isTokenInFollowSetOf("METHODS")) {
				currentToken++;
			}
			System.out.println("Recovered: Missing method parameter list, skipping to next method or class end");
		}
	}

	private void RULE_PARAMS() {
		System.out.println("------ RULE_PARAMS");
		
		// Los parámetros pueden estar vacíos (epsilon), así que verificamos si el token actual es ")"
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
			// Lista de parámetros vacía es válida, no hacer nada
			return;
		}
		
		if (!isTokenInFirstSetOf("PARAMS")) {
			boolean foundFirst = skipUntilFirstOrFollow("PARAMS", 600);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping PARAMS rule");
				return;
			}
		}
		
		if (isType()) {
			RULE_TYPE();
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("------ IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			} else {
				error(13);
				// Saltar hasta encontrar una coma o ")" para continuar
				while (currentToken < tokens.size() && 
					!tokens.get(currentToken).getValue().equals(",") && 
					!tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
				}
				
				if (currentToken >= tokens.size() || 
					(!tokens.get(currentToken).getValue().equals(",") && 
					!tokens.get(currentToken).getValue().equals(")"))) {
					System.out.println("Recovered: Malformed parameter, skipping parameter list");
					return;
				}
				System.out.println("Recovered: Found comma or closing parenthesis after missing parameter name");
			}
			
			while (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				System.out.println("------ ,");
				
				if (isType()) {
					RULE_TYPE();
					
					if (currentToken < tokens.size() && tokens.get(currentToken).getType().equals("IDENTIFIER")) {
						System.out.println("------ IDENTIFIER: " + tokens.get(currentToken).getValue());
						currentToken++;
					} else {
						error(14);
						// Saltar hasta encontrar una coma o ")" para continuar
						while (currentToken < tokens.size() && 
							!tokens.get(currentToken).getValue().equals(",") && 
							!tokens.get(currentToken).getValue().equals(")")) {
							currentToken++;
						}
						
						if (currentToken >= tokens.size() || 
							(!tokens.get(currentToken).getValue().equals(",") && 
							!tokens.get(currentToken).getValue().equals(")"))) {
							System.out.println("Recovered: Malformed parameter after comma, skipping parameter list");
							return;
						}
						System.out.println("Recovered: Found comma or closing parenthesis after missing parameter name");
					}
				} else {
					error(15);
					// Saltar hasta encontrar una coma o ")" para continuar
					while (currentToken < tokens.size() && 
						!tokens.get(currentToken).getValue().equals(",") && 
						!tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
					}
					
					if (currentToken >= tokens.size() || 
						(!tokens.get(currentToken).getValue().equals(",") && 
						!tokens.get(currentToken).getValue().equals(")"))) {
						System.out.println("Recovered: Missing parameter type after comma, skipping parameter list");
						return;
					}
					System.out.println("Recovered: Found comma or closing parenthesis after missing parameter type");
				}
			}
		}
	}

	private void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!(tokens.get(currentToken).getValue().equals("}")||tokens.get(currentToken).getValue().equals("break") || 
		        tokens.get(currentToken).getValue().equals("else"))) {
			if (isType()) {
				RULE_VARIABLE();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(16);
				}
			} else if (isAssignment()) {
				RULE_ASSIGNMENT();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(17);
				}
			} else if (isMethodCall()) {
				RULE_CALL_METHOD();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(18);
				}
			} else if (isReturnStatement()) {
				RULE_RETURN();
			} else if (isWhileStatement()) {
				RULE_WHILE();
			} else if (isIfStatement()) {
				RULE_IF();
			} else if (isDoStatement()) {
				RULE_DO_WHILE();
			} else if (isForStatement()) {
				RULE_FOR();
			} else if (isSwitchStatement()) {
				RULE_SWITCH();
			} else if (tokens.get(currentToken).getValue().equals("break")) {
				currentToken++;
				System.out.println("-- break");
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(20);
				}
			} else {
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("-- ;");
				} else {
					error(21);
				}
			}
		}
	}

	private void RULE_VARIABLE() {
		System.out.println("--- RULE_VARIABLE");
		RULE_TYPE();

		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;

			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("--- =");
				RULE_EXPRESSION();
			}
		} else {
			error(22);
		}
	}

	private void RULE_ASSIGNMENT() {
		System.out.println("--- RULE_ASSIGNMENT");
		
		if (!isTokenInFirstSetOf("ASSIGNMENT")) {
			boolean foundFirst = skipUntilFirstOrFollow("ASSIGNMENT", 600);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping ASSIGNMENT rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("--- =");
				
				if (!isTokenInFirstSetOf("EXPRESSION")) {
					boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 601);
					
					if (!foundFirst) {
						System.out.println("Recovered: Missing expression in assignment");
						return;
					}
				}
				
				RULE_EXPRESSION();
			} else {
				error(602);
				// Recuperación: buscar un punto y coma o el siguiente statement
				while (currentToken < tokens.size() && 
					!tokens.get(currentToken).getValue().equals(";") && 
					!isTokenInFollowSetOf("ASSIGNMENT")) {
					currentToken++;
				}
				System.out.println("Recovered: Skipped to next statement after invalid assignment");
			}
		} else {
			error(603);
			// Recuperación similar
			while (currentToken < tokens.size() && 
				!tokens.get(currentToken).getValue().equals(";") && 
				!isTokenInFollowSetOf("ASSIGNMENT")) {
				currentToken++;
			}
			System.out.println("Recovered: Skipped to next statement after invalid assignment");
		}
	}

	private void RULE_CALL_METHOD() {
		System.out.println("--- RULE_CALL_METHOD");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--- IDENTIFIER: " + tokens.get(currentToken).getValue());
			currentToken++;

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_PARAM_VALUES();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
				} else {
					error(25);
				}
			} else {
				error(26);
			}
		} else {
			error(27);
		}
	}

	private void RULE_PARAM_VALUES() {
		System.out.println("---- RULE_PARAM_VALUES");
		if (!tokens.get(currentToken).getValue().equals(")")) {
			RULE_EXPRESSION();

			while (tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				System.out.println("---- ,");
				RULE_EXPRESSION();
			}
		}
	}

	private void RULE_RETURN() {
		System.out.println("--- RULE_RETURN");
		
		if (!isTokenInFirstSetOf("RETURN")) {
			boolean foundFirst = skipUntilFirstOrFollow("RETURN", 800);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping RETURN rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("return")) {
			currentToken++;
			System.out.println("--- return");
			
			// Return puede tener una expresión opcional o ser simplemente "return;"
			if (currentToken < tokens.size() && !tokens.get(currentToken).getValue().equals(";")) {
				if (!isTokenInFirstSetOf("EXPRESSION")) {
					boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 801);
					
					if (!foundFirst) {
						// Si encontramos un punto y coma, eso está bien - tratarlo como "return;"
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
							currentToken++;
							System.out.println("--- ;");
							return;
						}
						
						// Saltar hasta encontrar un punto y coma o algo en FOLLOW(RETURN)
						while (currentToken < tokens.size() && 
							!tokens.get(currentToken).getValue().equals(";") && 
							!isTokenInFollowSetOf("RETURN")) {
							currentToken++;
						}
						
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
							currentToken++;
							System.out.println("--- ;");
						} else {
							System.out.println("Recovered: Missing expression and semicolon in return statement");
						}
						return;
					}
				}
				
				RULE_EXPRESSION();
			}
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("--- ;");
			} else {
				error(19);
				// Saltar hasta algo en FOLLOW(RETURN)
				while (currentToken < tokens.size() && !isTokenInFollowSetOf("RETURN")) {
					currentToken++;
				}
				System.out.println("Recovered: Missing semicolon after return statement");
			}
		} else {
			error(28);
			// Saltar hasta algo en FOLLOW(RETURN)
			while (currentToken < tokens.size() && !isTokenInFollowSetOf("RETURN")) {
				currentToken++;
			}
			System.out.println("Recovered: Invalid return statement");
		}
	}

	private void RULE_WHILE() {
		System.out.println("--- RULE_WHILE");
		
		if (!isTokenInFirstSetOf("WHILE")) {
			boolean foundFirst = skipUntilFirstOrFollow("WHILE", 900);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping WHILE rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("while")) {
			currentToken++;
			System.out.println("--- while");
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				
				if (!isTokenInFirstSetOf("EXPRESSION")) {
					boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 901);
					
					if (!foundFirst) {
						// Intentar recuperarse buscando el paréntesis de cierre
						while (currentToken < tokens.size() && 
							!tokens.get(currentToken).getValue().equals(")")) {
							currentToken++;
						}
						
						if (currentToken < tokens.size()) {
							currentToken++; // Saltar el paréntesis de cierre
							System.out.println("Recovered: Missing condition in while loop");
						} else {
							System.out.println("Recovered: Skipping malformed while loop");
							return;
						}
					} else {
						RULE_EXPRESSION();
					}
				} else {
					RULE_EXPRESSION();
				}
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					
					if (!isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						boolean foundFirst = skipUntilFirstOrFollow("STATEMENT_BLOCK", 902);
						
						if (!foundFirst) {
							System.out.println("Recovered: Missing statement block in while loop");
							return;
						}
					}
					
					RULE_STATEMENT_BLOCK();
				} else {
					error(29);
					// Intentar recuperarse buscando un bloque de código
					if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						System.out.println("Recovered: Missing ')' in while condition");
						RULE_STATEMENT_BLOCK();
					} else {
						// Saltar al siguiente statement
						while (currentToken < tokens.size() && 
							!isTokenInFollowSetOf("WHILE")) {
							currentToken++;
						}
						System.out.println("Recovered: Skipped malformed while loop");
					}
				}
			} else {
				error(30);
				// Intentar recuperarse comprobando si hay una expresión
				if (isTokenInFirstSetOf("EXPRESSION")) {
					System.out.println("Recovered: Missing '(' in while condition");
					RULE_EXPRESSION();
					
					if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
						System.out.println("--- )");
						RULE_STATEMENT_BLOCK();
					} else if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						System.out.println("Recovered: Missing ')' in while condition");
						RULE_STATEMENT_BLOCK();
					} else {
						// Saltar al siguiente statement
						while (currentToken < tokens.size() && 
							!isTokenInFollowSetOf("WHILE")) {
							currentToken++;
						}
						System.out.println("Recovered: Skipped malformed while loop");
					}
				} else {
					// Saltar al siguiente statement
					while (currentToken < tokens.size() && 
						!isTokenInFollowSetOf("WHILE")) {
						currentToken++;
					}
					System.out.println("Recovered: Skipped malformed while loop");
				}
			}
		} else {
			error(31);
		}
	}

	private void RULE_IF() {
		System.out.println("--- RULE_IF");
		
		if (!isTokenInFirstSetOf("IF")) {
			boolean foundFirst = skipUntilFirstOrFollow("IF", 400);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping IF rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("if")) {
			currentToken++;
			System.out.println("--- if");

			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				
				// Verificar si hay una expresión válida
				if (!isTokenInFirstSetOf("EXPRESSION")) {
					boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 401);
					if (!foundFirst) {
						// Intentar recuperarse y continuar
						System.out.println("Recovered: Missing expression in if condition");
					}
				} else {
					RULE_EXPRESSION();
				}

				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					
					RULE_STATEMENT_BLOCK();

					if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("else")) {
						currentToken++;
						System.out.println("--- else");
						
						// Verificar si hay un if después del else (else if)
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("if")) {
							RULE_IF(); // Manejo recursivo de else-if
						} else {
							RULE_STATEMENT_BLOCK();
						}
					}
				} else {
					error(402);
					// Intentar recuperarse y continuar con el bloque de código
					if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						System.out.println("Recovered: Missing closing parenthesis in if condition");
						RULE_STATEMENT_BLOCK();
						
						// Verificar si hay un else
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("else")) {
							currentToken++;
							System.out.println("--- else");
							
							if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("if")) {
								RULE_IF();
							} else {
								RULE_STATEMENT_BLOCK();
							}
						}
					}
				}
			} else {
				error(403);
				// Intentar recuperarse asumiendo que falta el paréntesis
				if (isTokenInFirstSetOf("EXPRESSION")) {
					System.out.println("Recovered: Missing opening parenthesis in if condition");
					RULE_EXPRESSION();
					
					// Buscar un paréntesis de cierre o directamente un bloque
					if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
						System.out.println("--- )");
					}
					
					// Continuar con el bloque de código
					if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						RULE_STATEMENT_BLOCK();
						
						// Verificar si hay un else
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("else")) {
							currentToken++;
							System.out.println("--- else");
							
							if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("if")) {
								RULE_IF();
							} else {
								RULE_STATEMENT_BLOCK();
							}
						}
					}
				}
			}
		} else {
			error(404);
		}
	}

	private void RULE_DO_WHILE() {
		System.out.println("--- RULE_DO_WHILE");
		
		if (!isTokenInFirstSetOf("DO_WHILE")) {
			boolean foundFirst = skipUntilFirstOrFollow("DO_WHILE", 900);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping DO_WHILE rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("do")) {
			currentToken++;
			System.out.println("--- do");
			
			if (!isTokenInFirstSetOf("STATEMENT_BLOCK")) {
				boolean foundFirst = skipUntilFirstOrFollow("STATEMENT_BLOCK", 901);
				
				if (!foundFirst) {
					// Saltar hasta "while" o algo en FOLLOW(DO_WHILE)
					while (currentToken < tokens.size() && 
						!tokens.get(currentToken).getValue().equals("while") && 
						!isTokenInFollowSetOf("DO_WHILE")) {
						currentToken++;
					}
					
					if (currentToken >= tokens.size() || !tokens.get(currentToken).getValue().equals("while")) {
						System.out.println("Recovered: Missing statement block and while part in do-while loop");
						return;
					}
					System.out.println("Recovered: Found while after missing statement block");
				} else {
					RULE_STATEMENT_BLOCK();
				}
			} else {
				RULE_STATEMENT_BLOCK();
			}
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("while")) {
				currentToken++;
				System.out.println("--- while");
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("(")) {
					currentToken++;
					System.out.println("--- (");
					
					if (!isTokenInFirstSetOf("EXPRESSION")) {
						boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 902);
						
						if (!foundFirst) {
							// Saltar a ")" o algo significativo
							while (currentToken < tokens.size() && 
								!tokens.get(currentToken).getValue().equals(")") && 
								!tokens.get(currentToken).getValue().equals(";") && 
								!isTokenInFollowSetOf("DO_WHILE")) {
								currentToken++;
							}
							
							if (currentToken >= tokens.size() || 
								(!tokens.get(currentToken).getValue().equals(")") && 
								!tokens.get(currentToken).getValue().equals(";"))) {
								System.out.println("Recovered: Missing condition in do-while loop");
								return;
							}
							System.out.println("Recovered: Found closing parenthesis or semicolon after missing condition");
						} else {
							RULE_EXPRESSION();
						}
					} else {
						RULE_EXPRESSION();
					}
					
					if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
						System.out.println("--- )");
						
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
							currentToken++;
							System.out.println("--- ;");
						} else {
							error(35);
							// Saltar hasta algo en FOLLOW(DO_WHILE)
							while (currentToken < tokens.size() && !isTokenInFollowSetOf("DO_WHILE")) {
								currentToken++;
							}
							System.out.println("Recovered: Missing semicolon after do-while condition");
						}
					} else {
						error(36);
						// Intentar recuperarse buscando el punto y coma
						while (currentToken < tokens.size() && 
							!tokens.get(currentToken).getValue().equals(";") && 
							!isTokenInFollowSetOf("DO_WHILE")) {
							currentToken++;
						}
						
						if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
							currentToken++;
							System.out.println("Recovered: Found semicolon after missing closing parenthesis");
						} else {
							System.out.println("Recovered: Missing closing parenthesis and semicolon in do-while");
						}
					}
				} else {
					error(37);
					// Intentar recuperarse saltando al siguiente statement
					while (currentToken < tokens.size() && !isTokenInFollowSetOf("DO_WHILE")) {
						currentToken++;
					}
					System.out.println("Recovered: Missing condition parentheses in do-while loop");
				}
			} else {
				error(38);
				// Intentar recuperarse saltando al siguiente statement
				while (currentToken < tokens.size() && !isTokenInFollowSetOf("DO_WHILE")) {
					currentToken++;
				}
				System.out.println("Recovered: Missing while part in do-while loop");
			}
		} else {
			error(39);
			// Intentar recuperarse saltando al siguiente statement
			while (currentToken < tokens.size() && !isTokenInFollowSetOf("DO_WHILE")) {
				currentToken++;
			}
			System.out.println("Recovered: Invalid do-while statement");
		}
	}

	private void RULE_FOR() {
		System.out.println("--- RULE_FOR");
		
		if (!isTokenInFirstSetOf("FOR")) {
			boolean foundFirst = skipUntilFirstOrFollow("FOR", 1000);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping FOR rule");
				return;
			}
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("for")) {
			currentToken++;
			System.out.println("--- for");
			
			if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				
				// Inicialización
				if (isType()) {
					RULE_VARIABLE();
				} else if (currentToken < tokens.size() && !tokens.get(currentToken).getValue().equals(";")) {
					if (!isTokenInFirstSetOf("EXPRESSION")) {
						boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 1001);
						
						if (foundFirst) {
							RULE_EXPRESSION();
						}
					} else {
						RULE_EXPRESSION();
					}
				}
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("--- ;");
				} else {
					error(40);
					// Saltar a la siguiente parte del bucle for
					while (currentToken < tokens.size() && 
						!tokens.get(currentToken).getValue().equals(";") && 
						!tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
					}
					
					if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
						currentToken++;
						System.out.println("Recovered: Found next semicolon in for loop");
					} else {
						System.out.println("Recovered: Missing semicolon in for loop initialization");
					}
				}
				
				// Condición
				if (currentToken < tokens.size() && !tokens.get(currentToken).getValue().equals(";")) {
					if (!isTokenInFirstSetOf("EXPRESSION")) {
						boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 1003);
						
						if (foundFirst) {
							RULE_EXPRESSION();
						}
					} else {
						RULE_EXPRESSION();
					}
				}
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("--- ;");
				} else {
					error(41);
					// Saltar hasta el paréntesis de cierre
					while (currentToken < tokens.size() && 
						!tokens.get(currentToken).getValue().equals(")")) {
						currentToken++;
					}
					
					if (currentToken < tokens.size()) {
						System.out.println("Recovered: Missing semicolon in for loop condition");
					} else {
						System.out.println("Recovered: Malformed for loop");
						return;
					}
				}
				
				// Incremento
				if (currentToken < tokens.size() && !tokens.get(currentToken).getValue().equals(")")) {
					if (isAssignment()) {
						RULE_ASSIGNMENT();
					} else if (!isTokenInFirstSetOf("EXPRESSION")) {
						boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 1005);
						
						if (foundFirst) {
							RULE_EXPRESSION();
						}
					} else {
						RULE_EXPRESSION();
					}
				}
				
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");
					
					if (!isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						boolean foundFirst = skipUntilFirstOrFollow("STATEMENT_BLOCK", 1006);
						
						if (!foundFirst) {
							System.out.println("Recovered: Missing statement block in for loop");
							return;
						}
					}
					
					RULE_STATEMENT_BLOCK();
				} else {
					error(42);
					// Intentar recuperarse buscando un bloque de código
					if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
						System.out.println("Recovered: Missing ')' in for loop");
						RULE_STATEMENT_BLOCK();
					} else {
						// Saltar al siguiente statement
						while (currentToken < tokens.size() && 
							!isTokenInFollowSetOf("FOR")) {
							currentToken++;
						}
						System.out.println("Recovered: Skipped malformed for loop");
					}
				}
			} else {
				error(43);
				// Intentar encontrar un bloque de código y asumir que el bucle for está incompleto
				while (currentToken < tokens.size() && 
					!isTokenInFirstSetOf("STATEMENT_BLOCK") && 
					!isTokenInFollowSetOf("FOR")) {
					currentToken++;
				}
				
				if (isTokenInFirstSetOf("STATEMENT_BLOCK")) {
					System.out.println("Recovered: Assuming empty for loop condition");
					RULE_STATEMENT_BLOCK();
				} else {
					System.out.println("Recovered: Skipped malformed for loop");
				}
			}
		} else {
			error(44);
		}
	}

	private void RULE_SWITCH() {
		System.out.println("--- RULE_SWITCH");
		if (tokens.get(currentToken).getValue().equals("switch")) {
			currentToken++;
			System.out.println("--- switch");

			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("--- (");
				RULE_EXPRESSION();

				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("--- )");

					if (tokens.get(currentToken).getValue().equals("{")) {
						currentToken++;
						System.out.println("--- {");

						while (!tokens.get(currentToken).getValue().equals("}")) {
							if (tokens.get(currentToken).getValue().equals("case")) {
								currentToken++;
								System.out.println("---- case");
								RULE_EXPRESSION();

								if (tokens.get(currentToken).getValue().equals(":")) {
									currentToken++;
									System.out.println("---- :");

									while (!tokens.get(currentToken).getValue().equals("break")) {
										RULE_BODY();
									}
									currentToken++;
									System.out.println("---- break");
									if (tokens.get(currentToken).getValue().equals(";")) {
										currentToken++;
										System.out.println("---- ;");
									}


								} else {
									error(45);
								}
							} else if (tokens.get(currentToken).getValue().equals("default")) {
								currentToken++;
								System.out.println("---- default");

								if (tokens.get(currentToken).getValue().equals(":")) {
									currentToken++;
									System.out.println("---- :");

									while (!tokens.get(currentToken).getValue().equals("}")) {
										RULE_BODY();
									}
								} else {
									error(46);
								}
							} else {
								error(47);
							}
						}

						if (tokens.get(currentToken).getValue().equals("}")) {
							currentToken++;
							System.out.println("--- }");
						} else {
							error(48);
						}
					} else {
						error(49);
					}
				} else {
					error(50);
				}
			} else {
				error(51);
			}
		} else {
			error(52);
		}
	}

	private void RULE_STATEMENT_BLOCK() {
		System.out.println("---- RULE_STATEMENT_BLOCK");
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("---- {");
			RULE_BODY();

			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("---- }");
			} else {
				error(53);
			}
		} else {
			if (isType()) {
				RULE_VARIABLE();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(54);
				}
			} else if (isAssignment()) {
				RULE_ASSIGNMENT();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(55);
				}
			} else if (isMethodCall()) {
				RULE_CALL_METHOD();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(56);
				}
			} else if (isReturnStatement()) {
				RULE_RETURN();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(57);
				}
			} else if (isWhileStatement()) {
				RULE_WHILE();
			} else if (isIfStatement()) {
				RULE_IF();
			} else if (isDoStatement()) {
				RULE_DO_WHILE();
			} else if (isForStatement()) {
				RULE_FOR();
			} else if (isSwitchStatement()) {
				RULE_SWITCH();
			} else {
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(";")) {
					currentToken++;
					System.out.println("---- ;");
				} else {
					error(58);
				}
			}
		}
	}

	private void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		
		if (!isTokenInFirstSetOf("EXPRESSION")) {
			boolean foundFirst = skipUntilFirstOrFollow("EXPRESSION", 1400);
			
			if (!foundFirst) {
				System.out.println("Recovered: Skipping EXPRESSION rule");
				return;
			}
		}
		
		RULE_X();
		
		while (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("||")) {
			currentToken++;
			System.out.println("--- ||");
			
			if (!isTokenInFirstSetOf("X")) {
				boolean foundFirst = skipUntilFirstOrFollow("X", 1401);
				
				if (!foundFirst) {
					System.out.println("Recovered: Missing operand after ||");
					break;
				}
			}
			
			RULE_X();
		}
	}

	private void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();

		while (currentToken < tokens.size() && 
		       !isEndOfExpression(tokens.get(currentToken).getValue()) && 
		       tokens.get(currentToken).getValue().equals("&&")) {
			currentToken++;
			System.out.println("---- &&");
			RULE_Y();
		}
	}

	private void RULE_Y() {
		System.out.println("----- RULE_Y");
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals("!")) {
			currentToken++;
			System.out.println("----- !");
			
			if (!isTokenInFirstSetOf("Y")) {
				boolean foundFirst = skipUntilFirstOrFollow("Y", 1420);
				
				if (!foundFirst) {
					System.out.println("Recovered: Missing operand after !");
					return;
				}
			}
			
			RULE_Y(); // Llamada recursiva para manejar múltiples !
		} else {
			if (!isTokenInFirstSetOf("R")) {
				boolean foundFirst = skipUntilFirstOrFollow("R", 1421);
				
				if (!foundFirst) {
					System.out.println("Recovered: Skipping Y rule");
					return;
				}
			}
			
			RULE_R();
		}
	}

	private void RULE_R() {
		System.out.println("------ RULE_R");
		RULE_E();

		while (currentToken < tokens.size() && !isEndOfExpression(tokens.get(currentToken).getValue())) {
			String currentVal = tokens.get(currentToken).getValue();
			if (currentVal.equals("<") || currentVal.equals(">") || 
				currentVal.equals("==") || currentVal.equals("!=") || 
				currentVal.equals("<=") || currentVal.equals(">=")) {
				
				System.out.println("------ " + currentVal);
				currentToken++;
				RULE_E();
			} else {
				break;
			}
		}
	}

	private void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();

		while (currentToken < tokens.size() && 
		       !isEndOfExpression(tokens.get(currentToken).getValue()) && 
		       (tokens.get(currentToken).getValue().equals("+") ||
				tokens.get(currentToken).getValue().equals("-"))) {
			System.out.println("------- " + tokens.get(currentToken).getValue());
			currentToken++;
			RULE_A();
		}
	}

	private void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();

		while (currentToken < tokens.size() && 
		       !isEndOfExpression(tokens.get(currentToken).getValue()) && 
		       (tokens.get(currentToken).getValue().equals("*") ||
				tokens.get(currentToken).getValue().equals("/"))) {
			System.out.println("-------- " + tokens.get(currentToken).getValue());
			currentToken++;
			RULE_B();
		}
	}

	private void RULE_B() {
		System.out.println("--------- RULE_B");
		if (currentToken < tokens.size() && 
		    !isEndOfExpression(tokens.get(currentToken).getValue()) && 
		    tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("--------- -");
		}
		RULE_C();
	}

	private void RULE_C() {
		System.out.println("---------- RULE_C");
		
		// Manejar el caso de operador de negación que llegue hasta aquí
		if (currentToken < tokens.size() && 
		    !isEndOfExpression(tokens.get(currentToken).getValue()) && 
		    tokens.get(currentToken).getValue().equals("!")) {
			System.out.println("---------- ! (en RULE_C)");
			currentToken++;
			RULE_C(); // Procesar recursivamente después del operador de negación
			return;
		}
		
		if (currentToken < tokens.size() && !isEndOfExpression(tokens.get(currentToken).getValue())) {
			if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				if (currentToken + 1 < tokens.size() && tokens.get(currentToken + 1).getValue().equals("(")) {
					RULE_CALL_METHOD();
				} else {
					System.out.println("---------- IDENTIFIER: " + tokens.get(currentToken).getValue());
					currentToken++;
				}
			} else if (tokens.get(currentToken).getType().equals("INTEGER") ||
					tokens.get(currentToken).getType().equals("FLOAT") ||
					tokens.get(currentToken).getType().equals("CHAR") ||
					tokens.get(currentToken).getType().equals("STRING") ||
					tokens.get(currentToken).getType().equals("HEXADECIMAL") ||
					tokens.get(currentToken).getType().equals("BINARY") ||
					tokens.get(currentToken).getType().equals("HEX") ||
					tokens.get(currentToken).getType().equals("OCTAL") ||
					(tokens.get(currentToken).getType().equals("KEYWORD") &&
							(tokens.get(currentToken).getValue().equals("true") ||
									tokens.get(currentToken).getValue().equals("false")))) {
				System.out.println("---------- LITERAL: " + tokens.get(currentToken).getValue());
				currentToken++;
			} else if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("---------- (");
				RULE_EXPRESSION();
				if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("---------- )");
				} else {
					error(59);
				}
			} else {
				error(60);
			}
		} else {
			// Si llegamos aquí y estamos al final o en un token especial (else, break, etc.)
			// no deberíamos generar un error, simplemente retornamos
			return;
		}
	}

	// Método para verificar si un token marca el final de una expresión
	private boolean isEndOfExpression(String tokenValue) {
		return tokenValue.equals(";") || 
            tokenValue.equals(")") || 
            tokenValue.equals("}") || 
            tokenValue.equals(",") || 
            tokenValue.equals(":") || 
            tokenValue.equals("else") || 
            tokenValue.equals("break") ||
            tokenValue.equals("case") ||
            tokenValue.equals("default");
	}

	private void RULE_TYPE() {
		System.out.println("----- RULE_TYPE");
		if (tokens.get(currentToken).getType().equals("KEYWORD") &&
				(tokens.get(currentToken).getValue().equals("int") ||
						tokens.get(currentToken).getValue().equals("float") ||
						tokens.get(currentToken).getValue().equals("void") ||
						tokens.get(currentToken).getValue().equals("char") ||
						tokens.get(currentToken).getValue().equals("string") ||
						tokens.get(currentToken).getValue().equals("boolean"))) {
			System.out.println("----- TYPE: " + tokens.get(currentToken).getValue());
			currentToken++;
		} else {
			error(61);
		}
	}

	private boolean isType() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				(tokens.get(currentToken).getValue().equals("int") ||
						tokens.get(currentToken).getValue().equals("float") ||
						tokens.get(currentToken).getValue().equals("void") ||
						tokens.get(currentToken).getValue().equals("char") ||
						tokens.get(currentToken).getValue().equals("string") ||
						tokens.get(currentToken).getValue().equals("boolean"));
	}

	private boolean isMethodDeclaration() {
		int savePos = currentToken;
		try {
			if (isType()) {
				currentToken++;
				if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
					currentToken++;
					return tokens.get(currentToken).getValue().equals("(");
				}
			}
			return false;
		} finally {
			currentToken = savePos;
		}
	}

	private boolean isAssignment() {
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (currentToken + 1 < tokens.size() &&
					tokens.get(currentToken + 1).getValue().equals("=")) {
				return true;
			}
		}
		return false;
	}

	private boolean isMethodCall() {
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (currentToken + 1 < tokens.size() &&
					tokens.get(currentToken + 1).getValue().equals("(")) {
				return true;
			}
		}
		return false;
	}

	private boolean isReturnStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("return");
	}

	private boolean isWhileStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("while");
	}

	private boolean isIfStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("if");
	}

	private boolean isDoStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("do");
	}

	private boolean isForStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("for");
	}

	private boolean isSwitchStatement() {
		return tokens.get(currentToken).getType().equals("KEYWORD") &&
				tokens.get(currentToken).getValue().equals("switch");
	}

	private void error(int error) {
		System.out.println("Syntax Error " + error +
				" at line " + tokens.get(currentToken).getLineNumber() +
				", token: " + tokens.get(currentToken).getValue());
		//System.exit(1)
	}
}
