public class UserView {

	private static final String BACK_SELECTED_TEXT_OUT = "Any previous entries for this term have been removed and memory is refreshed.";
	private static final String FLAG_SELECTED_TEXT_OUT = "Your next move will place a flag at the specified coordinate.";
	private static final String USER_COMMAND_PROMPT = "\nYour first number entry will be considered the row coordinate, "
			+ "\nyour second, the column coordinate. "
			+ "\nExample: 2, 3 makes an operation to the second row from the down, "
			+ "and the third column. "
			+ "\nIf you want to mark your next coordinate with a flag, "
			+ "\nenter the word flag, and then type in your coordinates. "
			+ "\nIf you want to remove any previous entries for this turn, "
			+ "type the word back. \nType help for a list of commands.";
	private static final char BOMB = 'B';
	private static final char FLAG = 'F';
	private static final char NULL_CHAR = '_';

	GameField game;

	char[][] userState;
	String[] format;
	int numBombs = 0, turn = 0, flags = 0;

	public UserView(GameField game) {
		this.game = game;
		this.userState = new char[game.getHeight()][game.getWidth()];
		this.format = new String[game.getHeight() + 1];
		fillNull();
		lookNice();
		printBoard(userState);
	}

	private void fillNull() {
		for (int i = 0; i < userState.length; i++) {
			for (int j = 0; j < userState[0].length; j++) {
				userState[i][j] = NULL_CHAR;
			}
		}

	}

	public void prompt() {
		while (true) {
			boolean cont = false;
			Integer row = -1;
			Integer column = -1;
			boolean notReady = true;
			boolean isFlag = false;
			String entry = null;
			if (turn == 0) {
				System.out
						.println("\nTime to sweep mines! Make your first move. "
								+ USER_COMMAND_PROMPT);
				System.out.println("There are " + numBombs
						+ " total mines on the field.");
				turn++;
			}
			while (notReady) {
				try {
					entry = IO.readString();
					switch (entry) {
					case "help":
						System.out.println(USER_COMMAND_PROMPT);
						break;
					case "back":
						row = -1;
						column = -1;
						System.out.println(BACK_SELECTED_TEXT_OUT);
						break;
					case "flag":
						System.out.println(FLAG_SELECTED_TEXT_OUT);
						isFlag = true;
						break;
					case "board":
						printBoard(userState);
						break;
					case "0":
						break;
					default:
						if (row == -1) {
							row = Integer.parseInt(entry) - 1;
							continue;
						} else
							column = Integer.parseInt(entry) - 1;
					}
				} catch (Exception NumberFormatException) {
					System.out
							.println("That is not a valid input choice. Please try again.");
				}
				if (row > -1 && row < game.getHeight() && column > -1
						&& column < game.getWidth()) {
					if (row <= game.getHeight() && column <= game.getWidth()) {
						if (isFlag) {
							if (checkFlag(row, column))
								break;
							cont = true;
						} else
							switch (game.actualState[row][column]) {
							case '0':
								cascade(row, column);
								cont = true;
								break;
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
								if (userState[row][column] == NULL_CHAR) {
									userState[row][column] = game.revealCell(
											row, column);
								} else {
									System.out
											.println("That tile has already been revealed.");
									turn--;
								}
								cont = true;
								break;
							case BOMB:
								userState[row][column] = game.revealCell(row,
										column);
								cont = true;
								break;
							}
						if (cont)
							break;
					}
				} else {
					System.out
							.println("Dimension error. Please enter input(s) again.");
					row = -1;
					column = -1;
					continue;

				}
			}
			if (cont)
				break;
		}
		System.out.println("There are " + (numBombs - flags)
				+ " mines left to triangulate.");

		turn++;

		System.out.println("It is now turn " + turn + ".");

	}

	private boolean checkFlag(int row, int column) {
		switch (userState[row][column]) {
		case FLAG:
			userState[row][column] = NULL_CHAR;
			flags--;
			break;
		case NULL_CHAR:
			userState[row][column] = FLAG;
			flags++;
			break;
		default:
			System.out
					.println("You are not allowed to flag a revealed tile. \nYour turn has been reset. \nPlease make another choice.");
			return true;
		}
		return false;
	}

	public void passNumBombs(int num) {
		numBombs = num;
	}

	public boolean checkLose() {
		for (int i = 0; i < userState.length; i++) {
			for (int j = 0; j < userState[0].length; j++) {
				if (userState[i][j] == BOMB) {
					return true;
				}
			}

		}
		return false;

	}

	public boolean checkWin() {
		int count = 0;
		for (int i = 0; i < userState.length; i++) {
			for (int j = 0; j < userState[0].length; j++) {
				if (userState[i][j] == NULL_CHAR || userState[i][j] == FLAG)
					count++;

			}
		}
		if (count == numBombs) {
			return true;
		}
		return false;
	}

	public void cascade(int row, int column) {

		for (int dy = -1; dy < 2; dy++) {
			for (int dx = -1; dx < 2; dx++) {
				if (game.checkBoardBounds(row + dy, column + dx)) {
					continue;
				}
				if (game.actualState[row + dy][column + dx] == '0') {
					if (userState[row + dy][column + dx] == NULL_CHAR) {
						userState[row + dy][column + dx] = '0';
						cascade(row + dy, column + dx);
						continue;
					}
				}
				if (userState[row + dy][column + dx] == NULL_CHAR) {
					userState[row + dy][column + dx] = game.actualState[row
							+ dy][column + dx];
				}

			}
		}

	}

	public void printBoard(char[][] selectedBoard) {
		int k;
		System.out.println();
		System.out.print(format[0]);
		for (int i = 0; i < userState.length; i++) {
			System.out.println();
			k = 0;
			for (int j = 0; j < userState[0].length;) {
				for (; k < format[i + 1].length(); k++) {
					System.out.print(format[i + 1].charAt(k));
					if (format[i + 1].charAt(k) == '[') {
						System.out.print(selectedBoard[i][j]);
						j++;
					}
				}
			}
		}

	}

	private void lookNice() {
		int hInd = 0;
		int x = game.getHeight();
		while (x > 0) {
			x = x / 10;
			hInd++;
		}
		format[0] = "";
		for (int j = 0; j <= hInd; j++) {
			format[0] += " ";
		}
		for (int i = 1; i <= game.getWidth(); i++) {
			if (i < 9)
				format[0] += i + "  ";
			else if (i < 100)
				format[0] += i + " ";
			else if (i < 1000)
				format[0] += i + "";
			else {
				System.out
						.println("Grid too large. Not able to configure to look nice.");
				format = null;
				return;
			}
		}
		for (int i = 0; i < game.getHeight(); i++) {
			x = hInd;
			format[i + 1] = "" + (i + 1);
			while (x > 0) {
				x--;
				format[i + 1] += " ";
			}
			x = i + 1;
			while (x > 0) {
				x = x / 10;
				format[i + 1] = format[i + 1].substring(0,
						format[i + 1].length() - 1);
			}
			for (int j = 0; j < game.getWidth(); j++) {
				format[i + 1] += "[]";
			}
		}

	}

}
