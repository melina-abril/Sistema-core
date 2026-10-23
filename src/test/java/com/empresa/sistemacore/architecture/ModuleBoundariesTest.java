package com.empresa.sistemacore.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ModuleBoundariesTest {
    @Test
    void businessDomainDoesNotDependOnSpring() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.empresa.sistemacore.modules");
        noClasses().that().resideInAPackage("..modules..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
                .check(classes);
    }
}
