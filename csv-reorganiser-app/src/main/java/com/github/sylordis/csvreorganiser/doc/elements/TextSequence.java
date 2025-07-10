package com.github.sylordis.csvreorganiser.doc.elements;

import java.util.Arrays;

/**
 * Text sequence is a sequence of different texts or other text sequences. The idea is that each
 * {@link TextSequence} in a {@link TextSequence} is a line on its own. The nesting should stop at
 * one level though.
 */
public class TextSequence extends Element {

	private BlockType type;
	private String qualifier;
	private int margin;

	/**
	 */
	public TextSequence() {
		this(BlockType.STANDARD, null);
	}

	/**
	 * @param type
	 */
	public TextSequence(BlockType type) {
		this(type, null);
	}

	/**
	 * @param type
	 * @param qualifier
	 */
	public TextSequence(BlockType type, String qualifier) {
		super();
		this.type = type;
		this.qualifier = qualifier;
	}

	/**
	 * Creates a simple text line.
	 * 
	 * @param contents
	 * @return
	 */
	public static TextSequence line(Text... contents) {
		return (TextSequence) new TextSequence().with(contents);
	}

	/**
	 * Creates a simple text line, converting each content entry as {@link Text} element.
	 * 
	 * @param contents every "word" or word block.
	 * @return
	 */
	public static TextSequence line(String... contents) {
		return (TextSequence) new TextSequence().with(Arrays.stream(contents).map(s -> new Text(s)).toList());
	}

	/**
	 * @return the type
	 */
	public BlockType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(BlockType type) {
		this.type = type;
	}

	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * @param qualifier the qualifier to set
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public TextSequence codeBlock(String type) {
		this.type = BlockType.CODE;
		this.qualifier = type;
		return this;
	}

	public TextSequence codeBlock() {
		return codeBlock(null);
	}

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * @param margin the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * @param margin the margin to set
	 * @return itself
	 */
	public TextSequence margin(int margin) {
		setMargin(margin);
		return this;
	}

}
