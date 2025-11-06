/*******************************************************************************
 * Copyright (c) 2011, 2023 Google, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Google, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wb.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

/**
 * Cyclic focus traversal policy based on array of components.
 * <p>
 * This class may be freely distributed as part of any application or plugin.
 * </p>
 *
 * @author scheglov_ke
 */
public class FocusTraversalOnArray extends FocusTraversalPolicy {
	private final Component m_Components[];
	////////////////////////////////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a new focus traversal policy using the given array of components.
	 *
	 * @param components array of components that defines the traversal order
	 */
	public FocusTraversalOnArray(Component components[]) {
		m_Components = components;
	}
	////////////////////////////////////////////////////////////////////////////
	//	Utilities
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the index that is distance {@code delta} away from {@code index}
	 * in a cyclic manner (wraps around the array bounds).
	 *
	 * @param index starting index
	 * @param delta offset to apply (can be negative)
	 * @return computed index after applying the delta with wrap-around
	 */
	private int indexCycle(int index, int delta) {
		int size = m_Components.length;
		int next = (index + delta + size) % size;
		return next;
	}
	
	/**
	 * Finds the next focusable component relative to the currently focused
	 * component, moving in the direction specified by {@code delta}.
	 * The search will return the first component that is enabled, visible
	 * and focusable. If none is found the current component is returned.
	 *
	 * @param currentComponent currently focused component (may be a child)
	 * @param delta direction to move (1 = forward, -1 = backward)
	 * @return the component to transfer focus to
	 */
	private Component cycle(Component currentComponent, int delta) {
		int index = -1;
		loop : for (int i = 0; i < m_Components.length; i++) {
			Component component = m_Components[i];
			for (Component c = currentComponent; c != null; c = c.getParent()) {
				if (component == c) {
					index = i;
					break loop;
				}
			}
		}
		// try to find enabled component in "delta" direction
		int initialIndex = index;
		while (true) {
			int newIndex = indexCycle(index, delta);
			if (newIndex == initialIndex) {
				break;
			}
			index = newIndex;
			//
			Component component = m_Components[newIndex];
			if (component.isEnabled() && component.isVisible() && component.isFocusable()) {
				return component;
			}
		}
		// not found
		return currentComponent;
	}
	////////////////////////////////////////////////////////////////////////////
	//	FocusTraversalPolicy
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the component that should receive the focus after the specified
	 * component when traversing forward.
	 */
	public Component getComponentAfter(Container container, Component component) {
		return cycle(component, 1);
	}
	
	/**
	 * Returns the component that should receive the focus before the specified
	 * component when traversing backward.
	 */
	public Component getComponentBefore(Container container, Component component) {
		return cycle(component, -1);
	}
	
	/**
	 * Returns the first component in the traversal cycle.
	 */
	public Component getFirstComponent(Container container) {
		return m_Components[0];
	}
	
	/**
	 * Returns the last component in the traversal cycle.
	 */
	public Component getLastComponent(Container container) {
		return m_Components[m_Components.length - 1];
	}
	
	/**
	 * Returns the default component to focus when the container receives focus.
	 */
	public Component getDefaultComponent(Container container) {
		return getFirstComponent(container);
	}
}