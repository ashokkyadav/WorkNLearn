import edu.princeton.cs.algs4.Picture;

/**
 * The {@code SeamCarver} class takes a <em>Picture</em> object as input and finds seams in it.
 * Seam can be vertical or horizontal. It searches for connected minimum energy pixels and returns.
 * Energy is calculated as summation of squares of x-gradient and y-gradient.
 * More info can be found at <a href="https://algs4.cs.princeton.edu/code/">Algo4 Princeton</a>
 * 
 * @author Ashok Yadav ashok.kyadav@gmail.com
 *
 */
public class SeamCarver {
    private static final double BORDER_ENERGY = 1000.0;
    private int[][] p;
    private double [][] energy;
    private boolean transposed;
    private int height;
    private int width;

    /**
     * @param picture: An instance of Picture class for which SeamCarving is needed.
     */
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.IllegalArgumentException("Argument to constructor has be to be a valid Picture object!");
        // store the picture in Color array
        p = new int[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                // System.out.println(i + " " + j);
                p[i][j] = picture.getRGB(j, i);
            }
        }
        transposed = false;
        energy = new double[picture.height()][picture.width()];
        height = picture.height();
        width = picture.width();
        computeAllEnergy();
    }

    /**
     * @return A Picture object representing the current state of the picture.
     */
    public Picture picture() {
        // System.out.println("Original width is " + width() + " height is " + height());
        Picture pic = new Picture(width(), height());
        // if(transposed) transposePicture();
        // transposed = false;
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                pic.setRGB(j, i, p[i][j]);
            }
        }
        return pic;
    }

    /**
     * @return Width of current picture
     */
    public     int width() {
        return width;
    }

    /**
     * @return Height of current picture
     */
    public     int height() {
        return height;
    }

    /**
     * @param x column of picture for which energy is asked for
     * @param y row of picture for which energy is asked for
     * @return energy at pixel[y][x]
     */
    public  double energy(int x, int y) {
        // Sanity check
        // System.out.println("x is " + x + " y is " + y + ". Width is " + width() + " height is " + height());
        if (x < 0 || x >= width()) throw new java.lang.IllegalArgumentException("Given coln " + x + " is outside of the picture created!");
        if (y < 0 || y >= height()) throw new java.lang.IllegalArgumentException("Given row " + y + " is outside of the picture created!");
        return energy[y][x];
    }
    
    /**
     * @return An array of integers representing row numbers for which the energy is minimum and are candidates for a seam.
     */
    public   int[] findHorizontalSeam() {
        // transpose the picture
        transposed = true;
        int[] seam = findVerticalSeam();
        transposed = false;
        return seam;
    }
    
    /**
     * @return sequence of indices for vertical seam
     */
    public   int[] findVerticalSeam() {
        // vertical seam found
        int[] seam = new int[currentHeight()];
        double minEnergy = Double.POSITIVE_INFINITY;
        double[][] distTo = new double[currentHeight()][currentWidth()];
        short[][] pathTo = new short[currentHeight()][currentWidth()];
        // Initialize all the distances
        for (int i = 0; i < currentHeight(); i++) 
            for (int j = 0; j < currentWidth(); j++) 
                distTo[i][j] = Double.POSITIVE_INFINITY;
        // loop through all pixels in row as starting point
        for (short x = 0; x < currentWidth(); x++) {
            getShortestPathDAG(x, distTo, pathTo);
        }
        // find the minimum energy path
        int minDistCol = 0;
        for (int i = 0; i < currentWidth(); i++) {
            if (minEnergy > distTo[currentHeight() - 1][i]) {
                minEnergy = distTo[currentHeight() - 1][i];
                minDistCol = i;
            }
        }
        // copy the path to the caller!
        seam[currentHeight() - 1] = minDistCol;
        for (int i = currentHeight() - 1; i > 0; i--) {
            seam[i - 1] = pathTo[i][minDistCol];
            minDistCol = pathTo[i][minDistCol];
        }
        return seam;
    }
    
    /**
     * @param seam sequence of row indices to be removed from current picture
     */
    public    void removeHorizontalSeam(int[] seam) {
        // Sanity check
        if (seam == null) throw new java.lang.IllegalArgumentException("Passed array cannot be null!");
        
        if (seam.length != width()) throw new java.lang.IllegalArgumentException("Seam provided does not match width of the picture!");
        
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] >= height()) throw new java.lang.IllegalArgumentException("Seam provided goes out of bounds!!");
        
        if (height() < 2) throw new java.lang.IllegalArgumentException("Cannot remove horizontal seam from a pic with 1 or less height!");
        
        // check if it's a valid seam. Distance between consecutive index can't be more than 1
        int index = seam[0];
        for (int i = 1; i < seam.length; i++) {
            int diff = seam[i] - index;
            if (diff > 1 || diff < -1) throw new java.lang.IllegalArgumentException("Cannot remove horizontal seam from a pic with 1 or less height!");
            index = seam[i];
        }
        
        removeCurrentHorizontalSeam(seam);

        // remove seam from energy matrix
        removeHorizontalSeamFromEnergy(seam);
        
        // update instance of height now
        height--;

        // need to update energies of adjacent pixels
        for (int col = 0; col < width(); col++) {
            // upper pixel
            if (seam[col] > 0) energy[seam[col] - 1][col] = computeEnergy(col, seam[col] - 1);
            // lower pixel
            if (seam[col] < height()) energy[seam[col]][col] = computeEnergy(col, seam[col]);
            if (seam[col] < height()) {
                // left pixel
                if (col > 0) energy[seam[col]][col - 1] = computeEnergy(col - 1, seam[col]);
                // right pixel
                if (col < width() - 1) energy[seam[col]][col + 1] = computeEnergy(col + 1, seam[col]);                
            }
        }
    }
    
    /**
     * @param seam sequence of column indices to be removed from current picture
     */
    public    void removeVerticalSeam(int[] seam) {
        // sanity check
        if (seam == null) throw new java.lang.IllegalArgumentException("Passed array cannot be null!");
        
        if (seam.length != height()) throw new java.lang.IllegalArgumentException("Seam provided does not match height of the picture!");
        
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] >= width()) throw new java.lang.IllegalArgumentException("Seam provided goes out of bounds!!");
        
        if (width() < 2) throw new java.lang.IllegalArgumentException("Cannot remove horizontal seam from a pic with 1 or less width!");
        
        // check if it's a valid seam. Distance between consecutive index can't be more than 1
        int index = seam[0];
        for (int i = 1; i < seam.length; i++) {
            int diff = seam[i] - index;
            if (diff > 1 || diff < -1) throw new java.lang.IllegalArgumentException("Cannot remove horizontal seam from a pic with 1 or less height!");
            index = seam[i];
        }

        // remove the seam from picture now!
        removeCurrentVerticalSeam(seam);
        
        // remove seam from energy matrix
        removeVerticalSeamFromEnergy(seam);

        // update instance of width now
        width--;

        // need to update energies of adjacent pixels
        for (int row = 0; row < height(); row++) {
            // left pixel
            if (seam[row] > 0) energy[row][seam[row] - 1] = computeEnergy(seam[row] - 1, row);
            // right pixel
            if (seam[row] < width()) energy[row][seam[row]] = computeEnergy(seam[row], row);
            if (seam[row] < width()) {
                // upper pixel
                if (row > 0) energy[row - 1][seam[row]] = computeEnergy(seam[row], row - 1);
                // lower pixel
                if (row < height() - 1) energy[row + 1][seam[row]] = computeEnergy(seam[row], row + 1);
            }
        }
    }
    
    // computes dual gradient energy at column x and row y
    /**
     * @param x
     * @param y
     * @return
     */
    private double computeEnergy(int x, int y) {
        // if it's a border pixel
        if (x == 0 || x == currentWidth() - 1) return BORDER_ENERGY;
        if (y == 0 || y == currentHeight() - 1) return BORDER_ENERGY;
        double xGradSqr = getXGradientSquared(y, x);
        double yGradSqr = getYGradientSquared(y, x);
        double enrgy = Math.sqrt(xGradSqr + yGradSqr);
        return enrgy;
    }
    // compute all energy
    private void computeAllEnergy() {
        for (int row = 0; row < p.length; row++) {
            for (int col = 0; col < p[0].length; col++) {
                energy[row][col] = computeEnergy(col, row);
            }
        }
    }
    
    // private methods to actually remove Vertical seam
    private void removeCurrentVerticalSeam(int[] seam) {
        // System.out.println("Rem Curr VerticalSeam currWidth " + currentWidth() + " currHeight is " + currentHeight());
        int[][] newPic = new int[currentHeight()][currentWidth() - 1];
        for (int i = 0; i < currentHeight(); i++) {
            int coln = 0;
            for (int j = 0; j < currentWidth(); j++) {
                if (seam[i] != j) newPic[i][coln++] = p[i][j];
            }
        }
        p = newPic;
        // System.out.println("Removed Curr VerticalSeam currWidth " + currentWidth() + " currHeight is " + currentHeight());
    }
    // private methods to actually remove Horizontal seam
    private void removeCurrentHorizontalSeam(int[] seam) {
        int[][] newPic = new int[currentHeight() - 1][currentWidth()];
        for (int i = 0; i < currentWidth(); i++) {
            int row = 0;
            for (int j = 0; j < currentHeight(); j++) {
                if (seam[i] != j) newPic[row++][i] = p[j][i];
            }
        }
        p = newPic;
    }
    private void removeHorizontalSeamFromEnergy(int[] seam) {
        double[][] newEnergy = new double[height() - 1][width()];
        for (int i = 0; i < width(); i++) {
            int row = 0;
            for (int j = 0; j < height(); j++) {
                if (seam[i] != j) newEnergy[row++][i] = energy[j][i];
            }
        }
        energy = newEnergy;        
    }
    private void removeVerticalSeamFromEnergy(int[] seam) {
        double[][] newEnergy = new double[height()][width() - 1];
        for (int i = 0; i < height(); i++) {
            int coln = 0;
            for (int j = 0; j < width(); j++) {
                if (seam[i] != j) newEnergy[i][coln++] = energy[i][j];
            }
        }
        energy = newEnergy;
    }
    private double getXGradientSquared(int x, int y) {
        // boundary check is already done!
        int colorLeft = p[x - 1][y];
        int colorRght = p[x + 1][y];
        double rx = (double) getRed(colorLeft) - (double) getRed(colorRght);
        rx *= rx;
        double gx = (double) getGreen(colorLeft) - (double) getGreen(colorRght);
        gx *= gx;
        double bx = (double) getBlue(colorLeft) - (double) getBlue(colorRght);
        bx *= bx;
        return (rx + gx + bx);
    }
    private double getYGradientSquared(int x, int y) {
        // boundary check is already done!
        int colorUp = p[x][y - 1];
        int colorDown = p[x][y + 1];
        double ry = (double) getRed(colorUp) - (double) getRed(colorDown);
        ry *= ry;
        double gy = (double) getGreen(colorUp) - (double) getGreen(colorDown);
        gy *= gy;
        double by = (double) getBlue(colorUp) - (double) getBlue(colorDown);
        by *= by;
        return (ry + gy + by);
    }
    // private method to get shorted path starting from top row!
    private void getShortestPathDAG(short x, double[][] distTo, short[][] pathTo) {
        // double minDist = Double.POSITIVE_INFINITY;
        distTo[0][x] = 0;
        // left and right limits of current row
        short left = x, right = x;
        short yStart = 0;
        // check each connected pixel in the height
        for (short i = 1; i < currentHeight(); i++) {
            int numRelaxed = 0;
            for (short j = left; j <= right; j++) {
                if (j > 0) if (relax(i, (short) (j - 1), j, yStart, distTo, pathTo)) {
                    if (j == left) numRelaxed++;
                }
                relax(i, j, j, yStart, distTo, pathTo);
                if (j < currentWidth() - 1) relax(i, (short) (j + 1), j, yStart, distTo, pathTo);
            }
            yStart = i;
            // update left and right
            left = (short) ((left > 0) ? (numRelaxed > 0) ? (left - 1) : left : left);
            right = (short) ((right < currentWidth() - 1) ? right + 1 : right);
        }
        return;
    }
    // relax edges at (y, x) y -> row, x -> coln of current picture which could be transposed!
    private boolean relax(short y, short x, short xStart, short yStart, double[][]distTo, short[][]pathTo) {
        int origRow = y;
        int origCol = x;
        boolean relaxed = false;
        if (transposed) { 
            origRow = x;
            origCol = y;
        }
        // if (origRow >= height()) throw new java.lang.IndexOutOfBoundsException("Row is out of bound -> " + origRow);
        // if (origCol >= width()) throw new java.lang.IndexOutOfBoundsException("Col is out of bound -> " + origCol);
        if (distTo[y][x] > (distTo[yStart][xStart] + energy[origRow][origCol])) {
            distTo[y][x] = distTo[yStart][xStart] + energy[origRow][origCol];
            pathTo[y][x] = xStart;
            relaxed = true;
        }
        return relaxed;
    }

    // returns current width of picture. current -> could be transposed!
    private int currentWidth() {
        if (transposed) return height;
        return width;
    }
    // returns current height of picture. current -> could be transposed!
    private int currentHeight() {
        if (transposed) return width;
        return height;
    }
    // getRed component from an int
    private int getRed(int color) {
        return ((color >> 16) & 0xff);
    }
    // get green component from an int
    private int getGreen(int color) {
        return ((color >> 8) & 0xff);
    }
    // get Blue component from an int
    private int getBlue(int color) {
        return (color & 0xff);
    }
}
