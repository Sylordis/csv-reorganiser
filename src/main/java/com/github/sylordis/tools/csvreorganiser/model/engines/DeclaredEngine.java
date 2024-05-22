package com.github.sylordis.tools.csvreorganiser.model.engines;

import com.github.sylordis.tools.csvreorganiser.model.chess.ChessEngine;
import com.github.sylordis.tools.csvreorganiser.model.hyde.HydeEngine;

/**
 * Enumeration of all engines.
 * 
 * @author sylordis
 *
 */
public enum DeclaredEngine {

	CHESS(ChessEngine.class, 1, "chess"),
	HYDE(HydeEngine.class, 2, "hyde");

	/**
	 * Declared type of the engine.
	 */
	private final Class<? extends ReorganiserEngine> engineType;
	/**
	 * Integer ID of the engine, it should be unique amongst all engines and positive.
	 */
	private final int id;
	/**
	 * All identifiers the engine can be called upon.
	 */
	private final String name;

	/**
	 * Constructor for an engine enum.
	 * 
	 * @param type the declared class of the engine
	 * @param ids  all IDs than can be used to get this engine
	 */
	private DeclaredEngine(Class<? extends ReorganiserEngine> type, int id, String name) {
		this.engineType = type;
		this.id = id;
		this.name = name;
	}

	/**
	 * Gets the engine type. This should never return null.
	 * 
	 * @return
	 */
	public Class<? extends ReorganiserEngine> getEngineType() {
		return engineType;
	}

	/**
	 * Gets the engine integer unique ID.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the IDs of an engine.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns an engine from its ID or null if it cannot be found. Strings comparison is done caseless.
	 * 
	 * @param needle the ID to match against
	 * @return
	 */
	public static DeclaredEngine getEngineTypeFromId(Object needle) {
		DeclaredEngine engine = null;
		// Search among all engine types
		for (DeclaredEngine currEngine : DeclaredEngine.values()) {
			if (needle != null) {
				int id = -1;
				String idstr = needle.toString().trim().toLowerCase();
				if (needle.getClass().equals(Integer.class) && (int) needle > 0)
					id = (int) needle;
				else
					try {
						id = Integer.valueOf((String) needle);
					} catch (ClassCastException | NumberFormatException e) {
						// Just continue searching, provided needle is just not an integer
					}
				// Match id or idstr
				if (id == currEngine.getId() || currEngine.getName().toLowerCase().equals(idstr)) {
					engine = currEngine;
					break;
				}
			}
		}
		return engine;
	}

}
