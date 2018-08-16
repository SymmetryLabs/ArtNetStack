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

/**
 * Controler Input Good.
 * @author Corentin Azelart.
 *
 */
public class ControllerGoodOutput {

	/** The merge mode is LTP. */
	private boolean mergeLTP;

	/** DMX output short detected on power up. */
	private boolean outputPowerOn;

	/** Output is merging ArtNet data. */
	private boolean outputmergeArtNet;

	/** This output accept DMX Text packets. */
	private boolean includeDMXTextPackets;

	/** This output accept DMX SIPs packets. */
	private boolean includeDMXSIPsPackets;

	/** This output accept DMX test packets. */
	private boolean includeDMXTestPackets;

	/** This output transmit data. */
	private boolean dataTransmited;

	/**
	 * Constructor.
	 */
	public ControllerGoodOutput() {
		super();
	}

	/**
	 * @return the mergeLTP
	 */
	public final boolean getMergeLTP() {
		return mergeLTP;
	}

	/**
	 * @param mergeLTP the mergeLTP to set
	 */
	public final void setMergeLTP(boolean mergeLTP) {
		this.mergeLTP = mergeLTP;
	}

	/**
	 * @return the outputPowerOn
	 */
	public final boolean getOutputPowerOn() {
		return outputPowerOn;
	}

	/**
	 * @param outputPowerOn the outputPowerOn to set
	 */
	public final void setOutputPowerOn(boolean outputPowerOn) {
		this.outputPowerOn = outputPowerOn;
	}

	/**
	 * @return the outputmergeArtNet
	 */
	public final boolean getOutputmergeArtNet() {
		return outputmergeArtNet;
	}

	/**
	 * @param outputmergeArtNet the outputmergeArtNet to set
	 */
	public final void setOutputMergeArtNet(final boolean outputmergeArtNet) {
		this.outputmergeArtNet = outputmergeArtNet;
	}

	/**
	 * @return the includeDMXTextPackets
	 */
	public final boolean getIncludeDMXTextPackets() {
		return includeDMXTextPackets;
	}

	/**
	 * @param includeDMXTextPackets the includeDMXTextPackets to set
	 */
	public final void setIncludeDMXTextPackets(final boolean includeDMXTextPackets) {
		this.includeDMXTextPackets = includeDMXTextPackets;
	}

	/**
	 * @return the includeDMXSIPsPackets
	 */
	public final boolean getIncludeDMXSIPsPackets() {
		return includeDMXSIPsPackets;
	}

	/**
	 * @param includeDMXSIPsPackets the includeDMXSIPsPackets to set
	 */
	public final void setIncludeDMXSIPsPackets(boolean includeDMXSIPsPackets) {
		this.includeDMXSIPsPackets = includeDMXSIPsPackets;
	}

	/**
	 * @return the includeDMXTestPackets
	 */
	public final boolean getIncludeDMXTestPackets() {
		return includeDMXTestPackets;
	}

	/**
	 * @param includeDMXTestPackets the includeDMXTestPackets to set
	 */
	public final void setIncludeDMXTestPackets(final boolean includeDMXTestPackets) {
		this.includeDMXTestPackets = includeDMXTestPackets;
	}

	/**
	 * @return the dataTransmited
	 */
	public final boolean getDataTransmited() {
		return dataTransmited;
	}

	/**
	 * @param dataTransmited the dataTransmited to set
	 */
	public final void setDataTransmited(final boolean dataTransmited) {
		this.dataTransmited = dataTransmited;
	}
}
