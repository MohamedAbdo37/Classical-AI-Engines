package com.algorithms.sudoko.models;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class printingDomains {

    BufferedWriter writer ;

    public printingDomains(BufferedWriter writer) {
        this.writer = writer;
    }

    public void printDomains(ArrayList<Integer>[][] domains) throws IOException {
        for(int i=0 ; i<9 ; i++){
            this.writer.write("+");
            for(int j=0 ; j<9 ; j++){
                this.writer.write("---------+");
            }
            this.writer.write("\n");
            this.writer.write("| ");
            for(int line =0 ; line<3 ; line++){
                for (int j=0 ; j<9 ; j++){
                    this.printLine(domains , line+1 , i , j);
                    this.writer.write(" | ");
                }
                this.writer.write("\n");
                if(line <=1)
                    this.writer.write("| ");
            }
        }
        this.writer.write("+");
        for(int j=0 ; j<9 ; j++){
            this.writer.write("---------+");
        }
        this.writer.write("\n");
    }
    private void printLine(ArrayList<Integer>[][] domains , int line , int row , int col)throws IOException{
        if(line==1)
            printElementInLine1(domains , row , col);
        else if(line==2)
            printElementInLine2(domains , row , col);
        else if(line==3)
            printElementInLine3(domains , row , col) ;
    }

    private void printElementInLine1(ArrayList<Integer>[][] domains , int row , int col) throws IOException {

        if(domains[row][col].size()==0){
            this.writer.write("   []   ");
        }

        else if(domains[row][col].size()==1){
            this.writer.write("  [" + domains[row][col].get(0)+"]  ");
        }
        else if(domains[row][col].size()==2){
            this.writer.write(" [" + domains[row][col].get(0)+"," +
                    domains[row][col].get(1)+"] ") ;
        }
        else if(domains[row][col].size()==3){
            this.writer.write("[" + domains[row][col].get(0)+"," +
                    domains[row][col].get(1)+"," +
                    domains[row][col].get(2)+"]") ;
        }
        else{
            this.writer.write("[" + domains[row][col].get(0)+"," +
                    domains[row][col].get(1)+"," +
                    domains[row][col].get(2)+",") ;
        }
    }

    private void printElementInLine2(ArrayList<Integer>[][] domains , int row , int col) throws IOException {

        if(domains[row][col].size()<=3){
            this.writer.write("       ");
        }

        else if(domains[row][col].size()==4){
            this.writer.write(" " + domains[row][col].get(3)+"]    ");
        }
        else if(domains[row][col].size()==5){
            this.writer.write(" " + domains[row][col].get(3)+"," +
                    domains[row][col].get(4)+"]  ") ;
        }
        else if(domains[row][col].size()==6){
            this.writer.write(" " + domains[row][col].get(3)+"," +
                    domains[row][col].get(4)+"," +
                    domains[row][col].get(5)+"]") ;
        }
        else{
            this.writer.write(" " + domains[row][col].get(3)+"," +
                    domains[row][col].get(4)+"," +
                    domains[row][col].get(5)+",") ;
        }
    }

    private void printElementInLine3(ArrayList<Integer>[][] domains , int row , int col) throws IOException {

        if(domains[row][col].size()<=6){
            this.writer.write("       ");
        }

        else if(domains[row][col].size()==7){
            this.writer.write(" " + domains[row][col].get(6)+"]    ");
        }
        else if(domains[row][col].size()==8){
            this.writer.write(" " + domains[row][col].get(6)+"," +
                    domains[row][col].get(7)+"]  ") ;
        }
        else if(domains[row][col].size()==9){
            this.writer.write(" " + domains[row][col].get(6)+"," +
                    domains[row][col].get(7)+"," +
                    domains[row][col].get(8)+"]") ;
        }
    }
}
