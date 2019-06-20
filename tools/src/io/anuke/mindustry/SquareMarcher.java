package io.anuke.mindustry;

import io.anuke.arc.files.FileHandle;
import io.anuke.arc.graphics.Pixmap;
import io.anuke.arc.graphics.g2d.*;
import io.anuke.arc.graphics.glutils.FrameBuffer;
import io.anuke.arc.util.*;

public class SquareMarcher{
    static final int resolution = 128;
    FrameBuffer buffer = new FrameBuffer(resolution, resolution);

    void render(Pixmap pixmap, FileHandle file){
        boolean[][] grid = new boolean[pixmap.getWidth()][pixmap.getHeight()];

        for(int x = 0; x < pixmap.getWidth(); x++){
            for(int y = 0; y < pixmap.getHeight(); y++){
                Tmp.c1.set(pixmap.getPixel(x, y));
                grid[x][pixmap.getHeight() - 1 - y] = Tmp.c1.a > 0.01f;
            }
        }

        Draw.flush();
        Draw.proj().setOrtho(0, 0, resolution, resolution);

        buffer.begin();
        float xscl = resolution / (float)pixmap.getWidth(), yscl = resolution / (float)pixmap.getHeight();
        float scl = xscl;

        for(int x = 0; x < pixmap.getWidth(); x++){
            for(int y = 0; y < pixmap.getHeight(); y++){
                int index = index(x, y, pixmap.getWidth(), pixmap.getHeight(), grid);

                float leftx = x * xscl, boty = y*yscl, rightx = x*xscl + xscl, topy = y*xscl + yscl,
                midx = x*xscl + xscl/2f, midy = y*yscl + yscl/2f;

                switch(index){
                    case 0:
                        break;
                    case 1:
                        Fill.tri(
                                leftx, midy,
                                leftx, topy,
                                midx, topy
                        );
                        break;
                    case 2:
                        Fill.tri(
                                midx, topy,
                                rightx, topy,
                                rightx, midy
                        );
                        break;
                    case 3:
                        Fill.crect(leftx, midy, scl, scl/2f);
                        break;
                    case 4:
                        Fill.tri(
                                midx, boty,
                                rightx, boty,
                                rightx, midy
                        );
                        break;
                    case 5:
                        //2 + 8
                        Fill.tri(
                                midx, topy,
                                rightx, topy,
                                rightx, midy
                        );

                        Fill.tri(
                                leftx, boty,
                                leftx, midy,
                                midx, boty
                        );
                        break;
                    case 6:
                        Fill.crect(midx, boty, scl/2f, scl);
                        break;
                    case 7:
                        //invert triangle
                        Fill.tri(
                                leftx, midy,
                                midx, midy,
                                midx, boty
                        );

                        //3
                        Fill.crect(leftx, midy, scl, scl/2f);

                        Fill.crect(midx, boty, scl/2f, scl/2f);
                        break;
                    case 8:
                        Fill.tri(
                                leftx, boty,
                                leftx, midy,
                                midx, boty
                        );
                        break;
                    case 9:
                        Fill.crect(leftx, boty, scl/2f, scl);
                        break;
                    case 10:
                        //1 + 4
                        Fill.tri(
                                leftx, midy,
                                leftx, topy,
                                midx, topy
                        );

                        Fill.tri(
                                midx, boty,
                                rightx, boty,
                                rightx, midy
                        );
                        break;
                    case 11:
                        //invert triangle

                        Fill.tri(
                                midx, boty,
                                midx, midy,
                                rightx, midy
                        );

                        //3
                        Fill.crect(leftx, midy, scl, scl/2f);

                        Fill.crect(leftx, boty, scl/2f, scl/2f);
                        break;
                    case 12:
                        Fill.crect(leftx, boty, scl, scl / 2f);
                        break;
                    case 13:
                        //invert triangle

                        Fill.tri(
                                midx, topy,
                                midx, midy,
                                rightx, midy
                        );

                        //12
                        Fill.crect(leftx, boty, scl, scl / 2f);

                        Fill.crect(leftx, midy, scl/2f, scl/2f);
                        break;
                    case 14:
                        //invert triangle

                        Fill.tri(
                                leftx, midy,
                                midx, midy,
                                midx, topy
                        );

                        //12
                        Fill.crect(leftx, boty, scl, scl / 2f);

                        Fill.crect(midx, midy, scl/2f, scl/2f);
                        break;
                    case 15:
                        Fill.square(midx, midy, scl * 2f);
                        break;
                }
            }
        }

        ScreenUtils.saveScreenshot(file, 0, 0, resolution, resolution);
        buffer.end();
    }

    int index(int x, int y, int w, int h, boolean[][] grid) {
        int top_left = Pack.byteValue(grid[x][y]);
        int top_right = x >= w - 1 ? 0 : Pack.byteValue(grid[x+1][y]);
        int bottom_right = x >= w - 1  || y >= h - 1 ? 0 : Pack.byteValue(grid[x+1][y+1]);
        int bottom_left = y >= h - 1 ? 0 : Pack.byteValue(grid[x][y+1]);
        return (bottom_left<<3) | (bottom_right<<2) | (top_right<<1) | top_left;
    }
}
