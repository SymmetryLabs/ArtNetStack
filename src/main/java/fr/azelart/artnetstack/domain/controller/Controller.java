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

import fr.azelart.artnetstack.constants.Constants;
import fr.azelart.artnetstack.domain.artnet.ArtNetObject;

import java.util.Map;

/**
 * General controler.
 * @author Corentin Azelart.
 */
public class Controller extends ArtNetObject {

	/** Port mapping. */
	private Map<Integer, ControllerPortType> portTypeMap;

	/**
	 * Set to false when video display is showing local data.
	 * Set to true when video is showing ethernet data.
	 */
	private boolean screen;

	/**
	 * Network identifier, 0-255.
	 */
	private int network = 0x00;

	/**
	 * Subnetwork identifier, 0-255.
	 */
	private int subNetwork = 0x0D;

	private String estaCode = "CZ";

	private String shortName = Constants.SHORT_NAME;

	private String longName = Constants.LONG_NAME;

	/**
	 * Controler.
	 */
	public Controller() {
		super();
	}

	/**
	 * @return the portTypeMap
	 */
	public final Map<Integer, ControllerPortType> getPortTypeMap() {
		return portTypeMap;
	}

	/**
	 * @param portTypeMap the portTypeMap to set
	 */
	public final void setPortTypeMap(final Map<Integer, ControllerPortType> portTypeMap) {
		this.portTypeMap = portTypeMap;
	}

	/**
	 * @return the screen
	 */
	public final boolean getScreen() {
		return screen;
	}

	/**
	 * Set to false when video display is showing local data.
	 * Set to true when video is showing ethernet data.
	 * @param screen the screen to set
	 */
	public final void setScreen(final boolean screen) {
		this.screen = screen;
	}

	/**
	 * @return the network
	 */
	public int getNetwork() {
		return network;
	}

	/**
	 * @param network the network to set
	 */
	public void setNetwork(final int network) {
		this.network = network;
	}

	/**
	 * @return the subNetwork
	 */
	public int getSubNetwork() {
		return subNetwork;
	}

	/**
	 * @param subNetwork the subNetwork to set
	 */
	public void setSubNetwork(final int subNetwork) {
		this.subNetwork = subNetwork;
	}

	public void setEstaCode(final String estaCode) {
		this.estaCode = estaCode;
	}

	public String getEstaCode() {
		return estaCode;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setLongName(final String longName) {
		this.longName = longName;
	}

	public String getLongName() {
		return longName;
	}
}