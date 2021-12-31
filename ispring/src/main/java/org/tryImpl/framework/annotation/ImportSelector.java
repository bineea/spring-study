package org.tryImpl.framework.annotation;

import java.lang.annotation.Annotation;

public interface ImportSelector {

    String[] selectImports(Annotation[] annotations);
}
