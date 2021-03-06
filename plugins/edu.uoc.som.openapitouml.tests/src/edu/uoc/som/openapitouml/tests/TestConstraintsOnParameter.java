package edu.uoc.som.openapitouml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.openapitouml.facade.OpenAPItoUMLFacade;

@DisplayName("Test constraints on Schema elements")
public class TestConstraintsOnParameter {

	private static ResourceSet RES_SET = new ResourceSetImpl();

	@BeforeAll
	private static void init() {
		RES_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		RES_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
	}

	@DisplayName("Test maximum")
	@Test
	public void testMaximum() {
		File input = new File("inputs/constraints/parameters.json");
		OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
		try {
			URI outputURI = URI.createFileURI("outputs/constraints");
			openAPItoUMLFacade.generateAndSaveClassDiagram(input, "parameters", outputURI, true);
			Resource res = RES_SET.getResource(outputURI.appendSegment("parameters").appendFileExtension("uml"), true);
			Model model = (Model) res.getContents().get(0);
			Class concept = (Class) model.getOwnedMember("Concept");
			Operation findConcepts = concept.getOperation("findConcepts", null, null);
			String constraintName = "findConcepts-parameterWithMaximum-maximumConstraint";
			String constraintValue = "self.parameterWithMaximum <= 10.0";
			assertAll("maximum", 
					() -> assertNotNull(findConcepts.getOwnedRule(constraintName)),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)findConcepts.getOwnedRule(constraintName)).getSpecification();
						assertTrue(expression.getBodies().get(0).equals(constraintValue));
					});
		
			} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@DisplayName("Test minimum")
	@Test
	public void testMinimum() {
		File input = new File("inputs/constraints/parameters.json");
		OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
		try {
			URI outputURI = URI.createFileURI("outputs/constraints");
			openAPItoUMLFacade.generateAndSaveClassDiagram(input, "parameters", outputURI, true);
			Resource res = RES_SET.getResource(outputURI.appendSegment("parameters").appendFileExtension("uml"), true);
			Model model = (Model) res.getContents().get(0);
			Class concept = (Class) model.getOwnedMember("Concept");
			Operation findConcepts = concept.getOperation("findConcepts", null, null);
			String constraintName = "findConcepts-parameterWithMinimum-minimumConstraint";
			String constraintValue = "self.parameterWithMinimum >= 10.0";
			assertAll("minimum", 
					() -> assertNotNull(findConcepts.getOwnedRule(constraintName)),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)findConcepts.getOwnedRule(constraintName)).getSpecification();
						assertTrue(expression.getBodies().get(0).equals(constraintValue));
					});
		
			} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	
}
