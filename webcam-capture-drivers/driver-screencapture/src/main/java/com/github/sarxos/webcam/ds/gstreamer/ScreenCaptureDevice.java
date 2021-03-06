package com.github.sarxos.webcam.ds.gstreamer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamException;


public class ScreenCaptureDevice implements WebcamDevice {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenCaptureDevice.class);

	private final GraphicsDevice device;
	private final DisplayMode mode;
	private final Dimension resolution;
	private final Robot robot;

	private boolean open = false;

	public ScreenCaptureDevice(final GraphicsDevice device) {

		this.device = device;
		this.mode = device.getDisplayMode();
		this.resolution = new Dimension(mode.getWidth(), mode.getHeight());

		try {
			this.robot = new Robot(device);
		} catch (AWTException e) {
			throw new WebcamException("Unable to create robot", e);
		}

		LOG.trace("Screen device {} with resolution {} has been created", getName(), getResolution());
	}

	@Override
	public String getName() {
		return device.getIDstring();
	}

	@Override
	public Dimension[] getResolutions() {
		return new Dimension[] { resolution };
	}

	@Override
	public Dimension getResolution() {
		return resolution;
	}

	@Override
	public void setResolution(Dimension size) {
		// do nothings, screen has only one resolution which is already set
	}

	@Override
	public BufferedImage getImage() {
		final GraphicsConfiguration gc = device.getDefaultConfiguration();
		final Rectangle bounds = gc.getBounds();
		return robot.createScreenCapture(bounds);
	}

	@Override
	public void open() {
		LOG.debug("Opening screen device {} with resolution {}", getName(), getResolution());
		open = true;
	}

	@Override
	public void close() {
		open = false;
	}

	@Override
	public void dispose() {
		// do nothing, no need to dispose anything here
	}

	@Override
	public boolean isOpen() {
		return open;
	}
}
