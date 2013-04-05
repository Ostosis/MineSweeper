public class GameField {

	private static final char EMPTY = ' ';

	public static final char BOMB = 'B';

	char[][] actualState;
	int x;

	public GameField(char[][] actualState, int diffRank) {
		this.actualState = actualState;
		this.x = diffRank;
		initializeBombs(diffRank);
		initializeCounts();
	}

	private void initializeBombs(int diffRank) {
		int diffNum = 0;
		switch (diffRank) {
		case 1:
			diffNum = 1;
			break;
		case 2:
			diffNum = 15;
			break;
		case 3:
			diffNum = 18;
			break;
		}
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				int rand = (int) (Math.random() * 100);
				if (rand < diffNum)
					actualState[i][j] = BOMB;
				else
					actualState[i][j] = EMPTY;
			}
		}
	}

	private void initializeCounts() {
		for (int row = 0; row < getHeight(); row++) {
			for (int column = 0; column < getWidth(); column++) {
				if (isBomb(row, column))
					continue;
				actualState[row][column] = (char) ('0' + countNeighborBombs(
						row, column));

			}
		}

	}

	public int getWidth() {
		return actualState[0].length;
	}

	public int getHeight() {
		return actualState.length;
	}

	public boolean isBomb(int row, int column) {
		if (row < 0 || row >= getHeight())
			return false;
		if (column < 0 || column >= getWidth())
			return false;
		return actualState[row][column] == BOMB;
	}

	public int countAllBombs() {
		int count = 0;
		for (int row = 0; row < getHeight(); row++) {
			for (int column = 0; column < getWidth(); column++) {
				if (actualState[row][column] == 'B')
					count++;
			}
		}
		return count;
	}

	public int countNeighborBombs(int row, int column) {
		int count = 0;
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) {
				if (isBomb(row + dy, column + dx)) {
					count++;
				}
			}
		}
		return count;
	}

	public char revealCell(int row, int column) {
		return actualState[row][column];
	}
	public boolean checkBoardBounds(int row, int column) {
		if (row < 0 || row >= getHeight())
			return true;
		if (column < 0 || column >= getWidth())
			return true;
		return false;
	}
	
}
