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
package fr.azelart.artnetstack.utils;

import fr.azelart.artnetstack.constants.Constants;
import fr.azelart.artnetstack.constants.MagicNumbers;
import fr.azelart.artnetstack.domain.arttimecode.ArtTimeCode;
import fr.azelart.artnetstack.domain.controller.Controller;
import fr.azelart.artnetstack.domain.controller.ControllerGoodInput;
import fr.azelart.artnetstack.domain.controller.ControllerGoodOutput;
import fr.azelart.artnetstack.domain.controller.ControllerPortType;
import fr.azelart.artnetstack.domain.enums.PortInputOutputEnum;
import fr.azelart.artnetstack.domain.enums.PortTypeEnum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.BitSet;
import java.util.Map;

/**
 * Encoder for ArtNet Packets.
 *
 * @author Corentin Azelart
 */
public final class ArtNetPacketEncoder {

	/**
	 * ArtPollCounter.
	 */
	private static volatile int artPollCounter = 1;

	/**
	 * ArtDmxCounter.
	 */
	private static volatile int artDmxCounter = 1;

	/**
	 * Private constructor to respect checkstyle and protect class.
	 */
	private ArtNetPacketEncoder() {
		super();
	}

	/**
	 * Encode an ArtPoll packet.
	 *
	 * @param controller is the controller
	 * @return the ArtPollPacket in array
	 * @throws IOException is the OutputStream have problem
	 */
	public static byte[] encodeArtPollPacket(final Controller controller) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(ByteUtils.toByta(Constants.ID));
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_32);
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(new Integer(Constants.ART_NET_VERSION).byteValue());
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_6);        // TalkToMe
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);    // Filler
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * Encode an ArtTimeCode packet.
	 *
	 * @param artTimeCode is timecode informations
	 * @return the ArtTimeCode in array
	 * @throws IOException in error with byte array
	 */
	public static byte[] encodeArtTimeCodePacket(final ArtTimeCode artTimeCode) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// ID.
		byteArrayOutputStream.write(ByteUtils.toByta(Constants.ID));
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// OpTimeCode
		byteArrayOutputStream.write(ByteUtilsArt.in16toByte(38656));

		// Version
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(new Integer(Constants.ART_NET_VERSION).byteValue());

		// Filler 1 and 2
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// Frame
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artTimeCode.getFrameTime()));

		// Seconds
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artTimeCode.getSeconds()));

		// Minutes
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artTimeCode.getMinutes()));

		// Hours
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artTimeCode.getHours()));

		// Type
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artTimeCode.getArtTimeCodeType().ordinal()));

		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * Encode a ArtDMX packet.
	 *
	 * @param universe is the universe
	 * @param network is the network
	 * @param dmx     is the 512 DMX parameters
	 * @return the ArtDmxCode in array
	 * @throws IOException in error with byte array
	 */
	public static byte[] encodeArtDmxPacket(
		final int universe,
		final int network,
		final int dmx[]
	) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Prepare next frame
		artDmxCounter++;

		// ID.
		byteArrayOutputStream.write(ByteUtils.toByta(Constants.ID));
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// OpOutput
		byteArrayOutputStream.write(ByteUtilsArt.in16toByte(20480));

		// Version
		byteArrayOutputStream.write(ByteUtilsArt.in16toBit(Constants.ART_NET_VERSION));

		// Sequence
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(artDmxCounter));

		// Physical
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// Net Switch
		byteArrayOutputStream.write(universe);
		byteArrayOutputStream.write(network);

		// DMX data Length
		byteArrayOutputStream.write(ByteUtilsArt.in16toBit(dmx.length));

		byte bdmx;
		for (int i = 0; i != Constants.DMX_512_SIZE; i++) {
			if (dmx.length > i) {
				bdmx = (byte) dmx[i];
				byteArrayOutputStream.write(ByteUtilsArt.in8toByte(bdmx));
			} else {
				byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
			}
		}

		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * Encode an ArtPollReply packet.
	 *
	 * @param controller is the controller
	 * @param inetAdress is the address informations
	 * @param port       is the port information
	 * @return the ArtTimeCode in array
	 * @throws IOException in error with byte array
	 */
	public static byte[] encodeArtPollReplyPacket(
		final Controller controller,
		final InetAddress inetAdress,
		final int port
	) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Prepare newt trame
		artPollCounter++;

		// ID.
		byteArrayOutputStream.write(ByteUtils.toByta(Constants.ID));
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// ArtPollReply
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_33);

		// IP
		byteArrayOutputStream.write(inetAdress.getAddress());

		// Port
		byteArrayOutputStream.write(ByteUtilsArt.in16toByte(port));

		// Version Hight
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(Constants.VERSION_LIB_HIGHT));

		// Version Low
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(Constants.VERSION_LIB_LOW));

		// Net Switch
		byteArrayOutputStream.write(controller.getNetwork());
		byteArrayOutputStream.write(controller.getSubNetwork());

		// Oem
		byteArrayOutputStream.write(ByteUtilsArt.hexStringToByteArray(("0x00ff")));

		// UBEA Version
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_ZERO);

		// Status1
		byteArrayOutputStream.write(0xf0);

		// Manufactor code
		// byteArrayOutputStream.write(ByteUtils.toByta(controller.getEstaCode()));
		byteArrayOutputStream.write(ByteUtilsArt.hexStringToByteArray("ff7f"));

		// ShotName
		byteArrayOutputStream.write(
			ByteUtils.toByta(
				encodeString(
					controller.getShortName(),
					Constants.MAX_LENGTH_SHORT_NAME
				)
			)
		);

		// LongName
		byteArrayOutputStream.write(
			ByteUtils.toByta(
				encodeString(
					controller.getLongName(),
					Constants.MAX_LENGTH_LONG_NAME
				)
			)
		);

		//Node report
		final int vArtPollCounter = artPollCounter + 1;
		final StringBuffer nodeReport = new StringBuffer();
		// nodeReport.append("#").append("0x0000");	// Debug mode, see table 3
		nodeReport.append("#").append("0x0001");	// Power On Tests successful, see table 3
		nodeReport.append("[").append(vArtPollCounter).append("]");
		nodeReport.append("ok");
		byteArrayOutputStream.write(
			ByteUtils.toByta(
				encodeString(
					nodeReport.toString(),
					Constants.MAX_LENGTH_NODE_REPORT
				)
			)
		);

		final Map<Integer, ControllerPortType> portsTypesMap = controller.getPortTypeMap();

		// NumPortHi (0, future evolution of ArtNet protocol)
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// NumPortLo (Between 0 and 4, max is 4)
		int numPorts = 0;
		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);
			if (controllerPortType != null) {
				numPorts = i+1;
			}
		}
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(numPorts));

		// Port Type
		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);
			// No port
			if (controllerPortType == null) {
				byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
			} else {
				BitSet bitSet = new BitSet(MagicNumbers.MAGIC_NUMBER_BITSET);
				// First 4 bits (PROCOTOL)
				if (controllerPortType.getType().equals(PortTypeEnum.DMX512)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, MagicNumbers.MAGIC_NUMBER_BIT_6, false);	// DMX
				} else if (controllerPortType.getType().equals(PortTypeEnum.MIDI)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, true);										// MIDI
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_1, MagicNumbers.MAGIC_NUMBER_BIT_6, false);	// MIDI
				} else if (controllerPortType.getType().equals(PortTypeEnum.AVAB)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, false);										// AVAB
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_1, true);										// AVAB
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_2, MagicNumbers.MAGIC_NUMBER_BIT_6, false);	// AVAB
				} else if (controllerPortType.getType().equals(PortTypeEnum.COLORTRANCMX)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, MagicNumbers.MAGIC_NUMBER_BIT_2, true);		// COLORTRAN
					bitSet.set(
						MagicNumbers.MAGIC_NUMBER_BIT_2,
						MagicNumbers.MAGIC_NUMBER_BIT_6,
						false
					);        // COLORTRAN
				} else if (controllerPortType.getType().equals(PortTypeEnum.ADB)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, MagicNumbers.MAGIC_NUMBER_BIT_2, false);	// ADB
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_2, true);                                        // ADB
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_3, MagicNumbers.MAGIC_NUMBER_BIT_6, false);	// ADB
				} else if (controllerPortType.getType().equals(PortTypeEnum.ARTNET)) {
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, true);										// ARTNET
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_1, false);										// ARTNET
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_2, true);                                        // ARTNET
					bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_3, MagicNumbers.MAGIC_NUMBER_BIT_6, false);	// ARTNET
				}

				PortInputOutputEnum direction = controllerPortType.getDirection();
				boolean outputEnabled = direction != null && (direction.equals(PortInputOutputEnum.OUTPUT)
								|| direction.equals(PortInputOutputEnum.BOTH));
				boolean inputEnabled = direction != null && (direction.equals(PortInputOutputEnum.INPUT)
								|| direction.equals(PortInputOutputEnum.BOTH));

				// Set if this channel can input onto the Art-Net Network
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_6, inputEnabled);
				// Set is this channel can output data from the Art-Net Network
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_7, outputEnabled);

				byteArrayOutputStream.write(toByteArray(bitSet));
			}
		}

		// Good Input
		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);
			ControllerGoodInput controllerGoodInput;
			// No port
			if (controllerPortType == null || (controllerGoodInput = controllerPortType.getGoodInput()) == null) {
				byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
			} else {
				BitSet bitSet = new BitSet(MagicNumbers.MAGIC_NUMBER_BITSET);
				bitSet.set(
					MagicNumbers.MAGIC_NUMBER_BIT_0,
					MagicNumbers.MAGIC_NUMBER_BIT_2,
					false
				);    // Unused and transmitted as zero
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_2, controllerGoodInput.getReceivedDataError());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_3, controllerGoodInput.getDisabled());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_4, controllerGoodInput.getIncludeDMXTextPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_5, controllerGoodInput.getIncludeDMXSIPsPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_6, controllerGoodInput.getIncludeDMXTestPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_7, controllerGoodInput.getDataReceived());
				byteArrayOutputStream.write(toByteArray(bitSet));
			}
		}

		// Good Ouput
		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);
			ControllerGoodOutput controllerGoodOutput;
			// No port
			if (controllerPortType == null || (controllerGoodOutput = controllerPortType.getGoodOutput()) == null) {
				byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
			} else {
				BitSet bitSet = new BitSet(MagicNumbers.MAGIC_NUMBER_BITSET);
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_0, false);    // Output is selected to transmit Art-Net.
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_1, controllerGoodOutput.getMergeLTP());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_2, controllerGoodOutput.getOutputShortDetected());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_3, controllerGoodOutput.getOutputmergeArtNet());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_4, controllerGoodOutput.getIncludeDMXTextPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_5, controllerGoodOutput.getIncludeDMXSIPsPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_6, controllerGoodOutput.getIncludeDMXTestPackets());
				bitSet.set(MagicNumbers.MAGIC_NUMBER_BIT_7, controllerGoodOutput.getDataTransmited());
				byteArrayOutputStream.write(toByteArray(bitSet));
			}
		}

		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);
			if (controllerPortType != null && controllerPortType.getDirection() != null
							&& (controllerPortType.getDirection().equals(PortInputOutputEnum.INPUT)
								|| controllerPortType.getDirection().equals(PortInputOutputEnum.BOTH))) {
				byteArrayOutputStream.write(controllerPortType.getUniverse());
			} else {
				// No port
				byteArrayOutputStream.write(0);
			}
		}

		for (int i = 0; i != Constants.MAX_PORT; i++) {
			ControllerPortType controllerPortType = portsTypesMap.get(i);

			if (controllerPortType != null && controllerPortType.getDirection() != null
							&& (controllerPortType.getDirection().equals(PortInputOutputEnum.OUTPUT)
								|| controllerPortType.getDirection().equals(PortInputOutputEnum.BOTH))) {
				byteArrayOutputStream.write(controllerPortType.getUniverse());
			} else {
				// No port
				byteArrayOutputStream.write(0);
			}
		}

		// Screen
		BitSet bitSet = new BitSet(MagicNumbers.MAGIC_NUMBER_BITSET);
		if (controller.getScreen()) {
			// Ethernet data display
			bitSet.set(1, true);
			byteArrayOutputStream.write(toByteArray(bitSet));

		} else {
			// Local data display
			byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		}

		// SwMacro (not implemented)
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// SwRemote (not implemented)
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// Spare (1+2+3), Not used, set to zero
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// Style
		// TODO
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// MAC
		NetworkInterface network = NetworkInterface.getByInetAddress(inetAdress);
		byteArrayOutputStream.write(network.getHardwareAddress());

		// Bind IP Address (not implemented)
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// Bind Index (not implemented)
		byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));

		// Status2
		// Node’s IP is DHCP configured.
		// Node is DHCP capable.
		// Node supports 8 bit Port-Address (ArtNet II).
		// Node not able to switch between ArtNet and sACN.
		byteArrayOutputStream.write(MagicNumbers.MAGIC_NUMBER_6);

		// Filler
		for (int i = 0; i < 26; i++) {
			byteArrayOutputStream.write(ByteUtilsArt.in8toByte(MagicNumbers.MAGIC_NUMBER_ZERO));
		}

		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * Encode string with finals white spaces.
	 *
	 * @param text is text
	 * @param size is max size
	 * @return the string
	 */
	private static String encodeString(final String text, final int size) {
		final StringBuffer sb = new StringBuffer();
		if (text.length() > size-1) {
			sb.append(text.subSequence(0, size-1));
		} else {
			sb.append(text);
		}
		for (int i = sb.length(); i < size; i++) {
			sb.append('\u0000');
		}
		return sb.toString();
	}

	private static byte[] toByteArray(BitSet bits) {
		byte[] bytes = new byte[(bits.length() + 7) / 8];
		for (int i = 0; i < bits.length(); i++) {
			if (bits.get(i)) {
				bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
			}
		}
		return bytes;
	}
}
