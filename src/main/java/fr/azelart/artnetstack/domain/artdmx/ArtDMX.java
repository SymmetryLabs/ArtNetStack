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
package fr.azelart.artnetstack.domain.artdmx;

import fr.azelart.artnetstack.constants.Constants;
import fr.azelart.artnetstack.constants.MagicNumbers;
import fr.azelart.artnetstack.domain.artnet.ArtNetObject;

/**
 * This is an ArtDMX packet.
 * @author Corentin Azelart
 *
 */
public class ArtDMX extends ArtNetObject {

	/**
	 * The sequence number is used to ensure that ArtDmx
	 * packets are used in the correct order. When Art-Net is
	 * carried over a medium such as the Internet, it is
	 * possible that ArtDmx packets will reach the receiver
	 * out of order.
	 */
	private int sequence;

	/**
	 * The Sequence field is set to 0x00 to disable this feature.
	 */
	private boolean sequenceEnabled;

	/**
	 * The physical input port from which DMX512 data was
	 * input. This field is for information only. Use Universe
	 * for data routing.
	 */
	private int physicalPort;

	/** Network. */
	private String net;

	/** Universe + SubNet in network. */
	private String subUni;
	private String universe;
	private String subNet;

	/**
	 * The length of the DMX512 data array. This value
	 * should be an even number in the range 2 - 512.
	 * It represents the number of DMX512 channels encoded
	 * in packet. NB: Products which convert Art-Net to
	 * DMX512 may opt to always send 512 channels.
	 * High Byte
	 */
	private int lengthHi;

	/**
	 * Low Byte of above.
	 */
	private int lengthLo;

	/**
	 * Length
	 */
	private int length;

	/**
	 * An variable length array of DMX512 lighting data.
	 */
	private int[] data;

	/**
	 * Construct an Art DMX packet.
	 */
	public ArtDMX() {
		super();
	}

	/**
	 * ToString method.
	 * @return a textual representation
	 */
	@Override
	public final String toString() {
		final StringBuilder vSb = new StringBuilder();
		vSb.append("ArtDMX[sequence=");
		vSb.append(sequence);
		vSb.append(",port=");
		vSb.append(physicalPort);
		vSb.append(",length=");
		vSb.append(length);
		if (data.length >= 1) {
			vSb.append(",C0=");
			vSb.append(data[0]);
		}
		if (data.length >= Constants.DMX_512_SIZE) {
			vSb.append(",C512=");
			vSb.append(data[Constants.DMX_512_SIZE - 1]);
		}
		vSb.append("]");
		return vSb.toString();
	}

	/**
	 * @return the sequence
	 */
	public final int getSequence() {
		return sequence;
	}

	/**
	 * @param pSequence the sequence to set
	 */
	public final void setSequence(final int pSequence) {
		this.sequence = pSequence;
	}

	/**
	 * @return the sequenceEnabled
	 */
	public final boolean isSequenceEnabled() {
		return sequenceEnabled;
	}

	/**
	 * @param pSequenceEnabled the sequenceEnabled to set
	 */
	public final void setSequenceEnabled(final boolean pSequenceEnabled) {
		this.sequenceEnabled = pSequenceEnabled;
	}

	/**
	 * @return the physicalPort
	 */
	public final int getPhysicalPort() {
		return physicalPort;
	}

	/**
	 * @param pPhysicalPort the physicalPort to set
	 */
	public final void setPhysicalPort(final int pPhysicalPort) {
		this.physicalPort = pPhysicalPort;
	}

	/**
	 * @return the net
	 */
	public final String getNet() {
		return net;
	}

	/**
	 * @param pNet the net to set
	 */
	public final void setNet(final String pNet) {
		this.net = pNet;
	}

	/**
	 * @return the subUni
	 */
	public final String getSubUni() {
		return subUni;
	}

	/**
	 * @param pSubUni the subUni to set
	 */
	public final void setSubUni(final String pSubUni) {
		this.subUni = pSubUni;
		int subUniInt = Integer.parseInt(pSubUni, MagicNumbers.MAGIC_NUMBER_16);
		this.subNet = Integer.toString((subUniInt >> 4) & 0xF, MagicNumbers.MAGIC_NUMBER_16);
		this.universe = Integer.toString(subUniInt & 0xF, MagicNumbers.MAGIC_NUMBER_16);
	}

	/**
	 * @return the universe
	 */
	public final String getUniverse() {
		return universe;
	}

	/**
	 * @return the subNet
	 */
	public final String getSubNet() {
		return subNet;
	}

	/**
	 * @return the lengthHi
	 */
	public final int getLengthHi() {
		return lengthHi;
	}

	/**
	 * @param pLengthHi the lengthHi to set
	 */
	public final void setLengthHi(final int pLengthHi) {
		this.lengthHi = pLengthHi;
		setLength(lengthHi, lengthLo);
	}

	/**
	 * @return the lengthLo
	 */
	public final int getLengthLo() {
		return lengthLo;
	}

	/**
	 * @param pLengthLo the length to set.
	 */
	public final void setLengthLo(final int pLengthLo) {
		this.lengthLo = pLengthLo;
		setLength(lengthHi, lengthLo);
	}

	/**
	 * @return the length
	 */
	public final int getLength() {
		return length;
	}

	public final void setLength(int highByte, int lowByte) {
		this.length = (highByte << 4) & lowByte;
	}

	/**
	 * @return the data
	 */
	public final int[] getData() {
		return data;
	}

	/**
	 * @param pData the data to set.
	 */
	public final void setData(final int[] pData) {
		this.data = pData;
	}
}
