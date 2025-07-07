package com.github.sylordis.csvreorganiser.model.hyde;

import java.util.List;
import java.util.function.UnaryOperator;

import com.github.sylordis.csvreorganiser.model.SelfFiller;

/**
 * @author sylordis
 *
 */
public interface HydeFilter extends UnaryOperator<String>, SelfFiller<List<Object>> {

}
