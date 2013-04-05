public class Minesweeper {
	private static final String LOSE_MSG = "\nAww. You lost.";
	private static final String WIN_MSG = "\nCongratulations! You won!";
	private static final String BOARD_ERROR = "BOARD ERROR";
//	private static final char FLAG = 'F';
//	private static final char BOMB = 'B';
//	private static final char NULL_CHAR = '\u0000';
	static int status = 0;

	public static void main(String[] args) throws NumberFormatException {
		int[] dimen = initialPrompt();
		char[][] actualState = new char[dimen[1]][dimen[0]];
		GameField game = new GameField(actualState, dimen[2]);
		UserView user = new UserView(game);
		if (user.format == null) {
			System.out.println(BOARD_ERROR);
			return;
		}
		System.out.println();
		user.passNumBombs(game.countAllBombs());
		while (status == 0) {
			user.prompt();
			user.printBoard(user.userState);
			System.out.println();
			if (user.checkWin()) {
				status = 1;
				break;
			}
			if (user.checkLose()) {
				status = -1;
				break;
			}
		}
		if (status == 1) {
			System.out.println(WIN_MSG);
			return;
		} else {
			System.out.println(LOSE_MSG);
			return;
		}
	}

	private static int[] initialPrompt() {
		// Initial prompt for user. Asks for difficulty and board dimensions
		// with error checks.
		String diff = null;
		System.out.println("Welcome to MineSweeper!");
		do {
			if (diff != null)
				System.out.println("Input error. Please try again.");
			System.out
					.print("\nSelect a difficulty. (Enter easy, medium, or hard.)\n");
			diff = IO.readString();
		} while (!(diff.equalsIgnoreCase("easy")
				|| diff.equalsIgnoreCase("medium") || diff
					.equalsIgnoreCase("hard")));
		int diffInt = 0;
		if (diff.equalsIgnoreCase("easy"))
			diffInt = 1;
		if (diff.equalsIgnoreCase("medium"))
			diffInt = 2;
		if (diff.equalsIgnoreCase("hard"))
			diffInt = 3;
		System.out
				.println("Now prepare to enter the dimensions of the board you would like to play with.");
		Integer widthCh = null;
		do {
			if (widthCh != null)
				System.out.println("Input error. Please try again.");
			System.out.println("Enter width.");
			widthCh = IO.readInt();
		} while (widthCh < 1);
		int width = widthCh.intValue();
		Integer heightCh = null;
		do {
			if (heightCh != null)
				System.out.println("Input error. Please try again.");
			System.out.println("Enter height.");
			heightCh = IO.readInt();
		} while (heightCh < 1);
		int height = heightCh.intValue();
		int[] dimen = new int[3];
		dimen[0] = width;
		dimen[1] = height;
		dimen[2] = diffInt;
		return dimen;
	}
}
