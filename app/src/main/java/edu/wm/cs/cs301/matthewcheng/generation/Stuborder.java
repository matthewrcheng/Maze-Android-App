package edu.wm.cs.cs301.matthewcheng.generation;

public class Stuborder implements Order {
	
	private int seed;
	
	private int skill;
	
	private boolean perfect;
	
	private Builder build;
	
	public Maze maze;

	int percent = 0;
	
	public Stuborder(int Seed, int Skill, boolean Perfect, Builder Build) {
		seed = Seed;
		skill = Skill;
		perfect = Perfect;
		build = Build;
	}
	
	@Override
	public int getSkillLevel() {
		// TODO Auto-generated method stub
		return skill;
	}

	public void setBuild(Builder build) {
		this.build = build;
	}

	@Override
	public Builder getBuilder() {
		// TODO Auto-generated method stub
		return build;
	}

	@Override
	public boolean isPerfect() {
		// TODO Auto-generated method stub
		return perfect;
	}

	@Override
	public int getSeed() {
		// TODO Auto-generated method stub
		return seed;
	}

	public Maze getMaze() {
		return maze;
	}

	@Override
	public void deliver(Maze mazeConfig) {
		// TODO Auto-generated method stub
		maze = mazeConfig;
	}

	@Override
	public void updateProgress(int percentage) {
		// TODO Auto-generated method stub
		if (percent < percentage && percentage <= 100) {
			percent = percentage;
		}
	}

	public int getProgress() {
		return percent;
	}
}
