/*
 * Copyright 2012 Corentin Azelart.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.azelart.artnetstack.domain.controller;

import fr.azelart.artnetstack.domain.enums.PortInputOutputEnum;
import fr.azelart.artnetstack.domain.enums.PortTypeEnum;

/**
 * Controler Port Type.
 * @author Corentin Azelart.
 *
 */
public class ControllerPortType {

	/** Port Number. */
	private int port;

	/** Port type. */
	private PortTypeEnum type;

	/** Direction. */
	private PortInputOutputEnum direction;

	/**
	 * @return the direction
	 */
	public final PortInputOutputEnum getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public final void setDirection(final PortInputOutputEnum direction) {
		this.direction = direction;
	}

	/** GoodInput. */
	private ControllerGoodInput goodInput;

	/** GoodOutput. */
	private ControllerGoodOutput goodOutput;

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public final void setPort(final int port) {
		this.port = port;
	}

	/**
	 * @return the type
	 */
	public final PortTypeEnum getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(final PortTypeEnum type) {
		this.type = type;
	}

	/**
	 * @return the goodInput
	 */
	public final ControllerGoodInput getGoodInput() {
		return goodInput;
	}

	/**
	 * @param goodInput the goodInput to set
	 */
	public final void setGoodInput(final ControllerGoodInput goodInput) {
		this.goodInput = goodInput;
	}

	/**
	 * @return the goodOutput
	 */
	public final ControllerGoodOutput getGoodOutput() {
		return goodOutput;
	}

	/**
	 * @param goodOutput the goodOutput to set
	 */
	public final void setGoodOutput(final ControllerGoodOutput goodOutput) {
		this.goodOutput = goodOutput;
	}
}
