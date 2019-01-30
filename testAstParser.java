//Name: Vishaal Bakshi
// UCID: 00305550
// Assignment 3
//SENG 300 


import org.eclipse.jdt.core.JavaCore;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;

public class testAstParser {

	ASTParser newParser = ASTParser.newParser(AST.JLS9);
	
    /**
     * Checks that pass1 readme can be constructed for JLS9.
     */
    @Test
    public void testCreateParserForJLS9() {
    		assertNotNull(ASTParser.newParser(AST.JLS9));
    }

    /**
     * Checks that pass1 readme cannot be constructed for "0", pass1 meaningless value for it.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateParserFor0() {
    		assertNotNull(ASTParser.newParser(0));    
    	}
  
    /**
     * Checks that environment can't be set when sourceCode.length doesn't match
     * with encoding.length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEnvironment() {
    		String [] classpathEntries = {"helloworld", "noworld"};
    		String [] encodings = {"UTF-8"};
    		String [] sourceCode = {"pass1.java", "pass2.java"};
    		newParser.setEnvironment(classpathEntries, sourceCode, encodings, true);		
    }
    
    /**
     * Checks that environment can be set
     */
    @Test
    public void testSetEnvironmentSourceCodeNull() {
    		String [] classpathEntries = {"helloworld", "noworld"};
    		String [] encodings = {"UTF-8"};
    		String [] sourceCode = {"pass1.java"};
    		newParser.setEnvironment(classpathEntries, sourceCode, encodings, true);		
    }
    
    /**
     * Checks different setters 
     */
    @Test
    public void testSetSource() {
	    newParser.setSource((ICompilationUnit) null);
	    newParser.setSource((IClassFile) null);
	    newParser.setSourceRange(0, 100);
		newParser.setProject(mock(IJavaProject.class));
		newParser.setBindingsRecovery(true);
		newParser.setFocalPosition(1);
		newParser.setStatementsRecovery(true);
		newParser.setStatementsRecovery(false);
		newParser.setResolveBindings(true);
		newParser.setIgnoreMethodBodies(false);
		newParser.setIgnoreMethodBodies(true);
		newParser.setWorkingCopyOwner(null);
		newParser.setWorkingCopyOwner(mock(WorkingCopyOwner.class));
    }
    
    /**
     * Check Bindings Recovery
     */
    @Test
    public void testBindingsRecovery() {
    		String source = "int foo;";
    		newParser.setResolveBindings(false);
    		newParser.setBindingsRecovery(false);
    		newParser.setSource(source.toCharArray());
    		newParser.setCompilerOptions(JavaCore.getOptions());
    		newParser.setUnitName("Trial1");
    		newParser.setKind(ASTParser.K_COMPILATION_UNIT);
    		CompilationUnit cu = (CompilationUnit) newParser.createAST(null);
    		assertEquals(false, cu.getAST().hasBindingsRecovery());
    }
    
    /**
     * Test invalid source range
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSourceRangeInvalid() {
    		newParser.setSourceRange(-1, 2);
    }
    
    /**
     * Test illegal argument for setKind
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetKindWrongArgument() {
    		newParser.setKind(-20);
    }
    
    /**
     * IllegalStateException when source is not provided
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateASTException() {
    		newParser.createAST(null);
    }
    
    /**
     * Try some source
     * with different types of kind.
     */
    @Test
    public void testCreateAST() {
    		// Try source as COMPILATION_UNIT
    		String source = "int pass1 = 5;";
    		newParser.setSource(source.toCharArray());
    		newParser.setCompilerOptions(null);
    		// Result node has to be Compilation Unit Type i.e. 15 
    		int expectedType = 15;
    		assertEquals(expectedType, newParser.createAST(null).getNodeType());
    		// Try source as statement
    		newParser.setKind(ASTParser.K_STATEMENTS);
    		source = "int pass1;";
    		newParser.setSource(source.toCharArray());
    		// Result node has to of BLOCK Type i.e. 5
    		int newExpectedType = 8;
    		assertEquals(newExpectedType, newParser.createAST(null).getNodeType());
    		// Try source as expression
    		newParser.setKind(ASTParser.K_EXPRESSION);
    		newParser.setSource(source.toCharArray());
    		assertEquals(expectedType, newParser.createAST(null).getNodeType());
    }
    
    /**
     * Try running source as other different kind
     */
    @Test
    public void testCreateASTMore() {
    		// Try source as class declaration
    		String source = "class Test {\n"+
    						"   public void foo() {}\n"+
    						"}\n";
    		newParser.setResolveBindings(true);
    		newParser.setSource(source.toCharArray());
    		newParser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
    		// Result node has to be of TYPE_DECLARATION type i.e 55
    		int expectedType = 55;
    		assertEquals(expectedType, newParser.createAST(null).getNodeType());
    }
   
    /**
     * Test IllegalStateException when source is null
     * @throws JavaModelException
     */
	@Test(expected = IllegalStateException.class)
    public void testSetSourceNullSource() throws JavaModelException {
    		Map <String, String> options = new HashMap<String, String>();
    		ITypeRoot source = mock(ITypeRoot.class, withSettings().extraInterfaces(IClassFile.class));
    		IJavaProject project = mock(IJavaProject.class);
    		when(project.getOptions(true)).thenReturn(options);
    		when(source.getSource()).thenReturn(null);
    		when(source.getJavaProject()).thenReturn(project);    		
    		// Set the source for IllegalStateException when source string is null
    		newParser.setSource(source);
    		newParser.createAST(null);
    }
	
	/**
	 * Test IllegalStateException when project is null
	 */
	@Test(expected = IllegalStateException.class)
	public void testCreateBindingsIllegalState() {
		newParser.createBindings(null, null);
	}
	
	/**
	 * Test IllegalStateException in createASTs
	 */
	@Test(expected = IllegalStateException.class)
	public void testCreateASTs() {
		newParser.setResolveBindings(true);
		newParser.setProject(null);
		newParser.createASTs(null, null, null, null);
	}
}