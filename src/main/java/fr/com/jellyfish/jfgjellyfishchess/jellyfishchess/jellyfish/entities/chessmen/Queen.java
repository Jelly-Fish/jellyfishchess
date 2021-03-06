/**
 * *****************************************************************************
 * Copyright (c) 2014, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 ******************************************************************************
 */
package fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.entities.chessmen;

import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.constants.BoardConst;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.constants.CommonConst;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.entities.Board;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.entities.Position;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Describes the queen's moves and possibilities.
 *
 * @author Thomas.H Warner 2014
 */
public class Queen extends AbstractChessMan {

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor.
     *
     * @param COLOR
     * @param valuation
     * @param alive
     * @param boardPosition
     * @param nullChessMan
     * @param fenValue
     */
    public Queen(final String COLOR, final float valuation, final boolean alive,
            final Position boardPosition, final boolean nullChessMan, final char fenValue) {
        super(COLOR, valuation, alive, boardPosition, nullChessMan, fenValue);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Overriden methods">
    /**
     * Evaluate move for queen : is it valid considering chess rules ? If not
     * return false will throw an InvalidMoveException.
     *
     * @param xyFrom
     * @param xyTo
     * @return
     */
    @Override
    public boolean isValidMove(final Integer[] xyFrom, final Integer[] xyTo) {
        // Check Position class's between from & too for null chessmen.
        // ! Color has no importance here, a rook moves the same way if it's
        // black or white.

        boolean rookTypeMove = false;
        boolean bishopTypeMove = false;
        // Is bishop type valid move.
        boolean diagonalMove = false;
        // Rook moves.
        boolean upDown = false;
        boolean leftRight = false;

        int diagonal = 0; // 4 diagonals are possible.
        char direction = CommonConst.DUMMY; // For rook type moves.
        List<Integer[]> positions = new ArrayList<>();

        //<editor-fold defaultstate="collapsed" desc="Duplicated code from Rook & Bishop class's: prepare validation">
        // First, if it is a rook type move then check for valid rook move. Else
        // it is a bishop type move. Can't be both...
        // If move is on a straight line then it's a rook move : 
        if (xyFrom[0].equals(xyTo[0]) || xyFrom[1].equals(xyTo[1])) {
            rookTypeMove = true;

            // Is it right, left, up or down move ?
            // 'u' == up, 'r' == right ect.
            if (xyFrom[0].equals(xyTo[0])) { // Left or right ?
                direction = xyFrom[1] < xyTo[1] ? CommonConst.R_CHAR_LOWERCASE : CommonConst.L_CHAR_LOWERCASE;
                leftRight = true;
            } else { // up or down ?
                direction = xyFrom[0] < xyTo[0] ? CommonConst.D_CHAR_LOWERCASE : CommonConst.U_CHAR_LOWERCASE;
                upDown = true;
            }
        } else {
            bishopTypeMove = true;
            // 1: up.right - 2: down.right - 3: up.left - 4: down.left
            if (xyFrom[1] < xyTo[1]) { // Left or right ?
                // It's right.
                diagonal = xyFrom[0] > xyTo[0] ? 1 : 2; // Up or down ?
            } else if (xyFrom[1] > xyTo[1]) {
                // Move is left.
                diagonal = xyFrom[0] > xyTo[0] ? 3 : 4; // Up or down ?
            }
        }

        if (rookTypeMove) {
            positions = getRookPositions(direction, xyFrom, xyTo);

            // Check for valid rook move : straight lines whatever the direction is.
            if (upDown && !(xyFrom[1].equals(xyTo[1]))) {
                return false;
            } else if (leftRight && !(xyFrom[0].equals(xyTo[0]))) {
                return false;
            }
        } else if (bishopTypeMove) {
            positions = getBihopDiagonals(diagonal, xyFrom, xyTo);

            // Check if xyTo[] is contained in the numeric positions collection.
            for (int i = 0; i < positions.size(); ++i) {
                if (positions.get(i)[0].equals(xyTo[0]) && positions.get(i)[1].equals(xyTo[1])) {
                    diagonalMove = true;
                    break;
                }
            }
        } else {
            return false;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Duplicated code from Rook & Bishop class's: validation">
        // If previous check does not validate a diagonal bishop move, then
        // move is invalid : return false. If the move is rook type then validate it.
        int counter = 0;
        if (diagonalMove && bishopTypeMove || rookTypeMove) {

            // Now check all positions. Count then and compare with positions array
            // list size(). If last move, check for != color as for an attack move.
            final LinkedHashMap<String, Position> coordinates = Board.getInstance().getCoordinates();
            for (int i = 0; i < positions.size(); ++i) {
                
                if (!coordinates.get(BoardConst.getPostionFromIntegers(positions.get(i))
                    ).getOnPositionChessMan().isNullChessMan()) {
                    // Here loop has encountered a chess man :
                    // Check for attack. Meaning if it is last loop then check that
                    // position is occupied by a != color chessman.

                    if (i + 1 == positions.size()
                            && !coordinates.get(BoardConst.getPostionFromIntegers(positions.get(i))
                            ).getOnPositionChessMan().getColor().equals(this.getColor())) {
                        // It is a valid attack situation.
                        // We are on last position check, meaning all previous
                        // positions have been checked. Move is validated :
                        return true;
                    } else {
                        return false;
                    }

                } else { // Position is free.
                    // Add 1 to counter.
                    ++counter;
                }
            }

        } else { // diagonal bishop move is invalid.
            return false;
        }
        //</editor-fold>

        // Finally compare counter and array list size. If == then the move can
        // be validated.
        return counter == positions.size();
        // Move has been checked step by step and is valid.
    }
    //</editor-fold>

}
