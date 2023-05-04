package com.github.sylordis.tools.csvreorganiser.model.chess.operations;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Interface for classes able to instantiate an operation from its name and YAML data. This is
 * pretty much a type definition for what it extends.
 *
 * @author sylordis
 * @since 1.1
 *
 */
public interface ChessOperationInstantiator extends BiFunction<String, Map<String, Object>, ChessAbstractReorgOperation> {

}
