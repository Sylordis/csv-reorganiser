package com.github.sylordis.tools.csvreorganiser.model.engines;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import com.github.sylordis.tools.csvreorganiser.model.chess.ChessEngine;
import com.github.sylordis.tools.csvreorganiser.model.exceptions.ConfigurationException;

/**
 * Factory to provide Engines.
 * 
 * @author sylordis
 * @since 1.1
 *
 */
public class EngineFactory {

	/**
	 * Class logger
	 */
	private final Logger logger = LogManager.getLogger(); 
	
	/**
	 * Gets the default engine from provided id, if it matches.
	 * 
	 * @see DeclaredEngine
	 * 
	 * @param id the id
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public ReorganiserEngine getEngineFromId(Object id) {
		DeclaredEngine engineType = DeclaredEngine.getEngineTypeFromId(id);
		ReorganiserEngine engine = null;
		if (engineType != null) {
			try {
				Class<? extends ReorganiserEngine> engineDeclaredType = engineType.getEngineType();
				engine = engineDeclaredType.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				Message msg = logger.getMessageFactory().newMessage(
						"Provided ID matches an engine ({}) but engine cannot be retrieved", engineType.getEngineType());
				throw new ConfigurationException(msg.getFormattedMessage());
			}
		}
		return engine;
	}

	/**
	 * Gets the default engine for the reorganiser.
	 * 
	 * @return
	 */
	public static ReorganiserEngine getDefaultEngine() {
		return ChessEngine.createDefaultEngine();
	}

}
