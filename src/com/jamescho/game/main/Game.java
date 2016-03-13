package com.jamescho.game.main;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import com.jamescho.game.state.State;
import com.jamescho.game.state.LoadState;
import com.jamescho.framework.util.InputHandler;


@SuppressWarnings("serial")

public class Game extends JPanel implements Runnable {
	
	private int gameWidth;
	private int gameHeight;
	private Image gameImage;
	
	private Thread gameThread;
	private volatile boolean running;
	private volatile State currentState;
	private InputHandler inputHandler;
	
	public Game(int gameWidth, int gameHeight)
	{
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		setPreferredSize(new Dimension(gameWidth, gameHeight));
		
		setBackground(Color.BLACK);
		setFocusable(true);
		requestFocus();
	}
	
	public void setCurrentState(State newState)
	{
		System.gc();
		currentState = newState;
		newState.init();
		inputHandler.setCurrentState(currentState);
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		initInput();
		setCurrentState(new LoadState());
		initGame();
	}
		
	public void initGame()
	{
		running = true;
		gameThread = new Thread(this, "Game Thread");
		gameThread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running Game");
		
		while(running)
		{
			currentState.update();
			prepareGameImage();
			
			currentState.render(gameImage.getGraphics());
			repaint();
			
			try
			{
				Thread.sleep(14);
				
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		System.exit(0);
		
	}
	
	public void exit()
	{
		running = false;
	}
	
	private void prepareGameImage()
	{
		if(gameImage == null)
		{
			gameImage = createImage(gameWidth, gameHeight);
		}
		
		Graphics g = gameImage.getGraphics();
		
		g.clearRect(0, 0, gameWidth, gameHeight);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(gameImage == null)
		{
			return;
		}
		
		g.drawImage(gameImage, 0, 0, null);
	}
	
	private void initInput()
	{
		inputHandler = new InputHandler();
		addKeyListener(inputHandler);
		addMouseListener(inputHandler);
	}
	
}

