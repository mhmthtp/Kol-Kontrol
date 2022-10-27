import socket
from pyfirmata import Arduino ,SERVO,util
from time import sleep
import cv2
import threading

ARDUINO_PORT='COM3' 

PIN1=10 
PIN2=11
PIN3=9
PIN4=6

HOST = "***" 
SOCKET_PORT=5678

motorMerkez = 90 
motorSol = 90
motorSag = 90
motorPence = 60

def rotateservo(board, pin,angle):
    print("MOTOR_MERKEZ:", motorMerkez, "MOTOR_SOL:", motorSol, 
            "MOTOR_SAG:", motorSag, "MOTOR_PENCE:", motorPence)

    board.digital[pin].write(angle) 
    sleep(0.015)

def androidkomut(sckt, board):
   while 1:
    conn, addr = sckt.accept()
    if conn:
        ldata, addr2 = conn.recvfrom(256)
        if not ldata:
            break
        alinanKomut = ldata.decode()
        conn.close() 
        motorKontrol(board, alinanKomut.lstrip('\x00\x08'))



def motorKontrol(board, komut):
    global motorMerkez
    global motorSol
    global motorSag
    global motorPence

    while True:
        if komut=="00000001":
            motorMerkez +=10
            if motorMerkez >= 0 and motorMerkez <= 180:
                return rotateservo(board, PIN1, motorMerkez)
            else:
                print("max donus")
        elif komut=="00000002":
            motorMerkez -=10
            if motorMerkez >= 0 and motorMerkez <= 180:
                return rotateservo(board,PIN1,motorMerkez)
            else:
                print("max donus")
        elif komut=="00000003":
            motorSol +=10
            if motorSol >= 0 and motorSol <= 180:
                return rotateservo(board,PIN2,motorSol)
        elif komut=="00000004":
            motorSol -= 10
            if motorSol >= 0 and motorSol <= 180:
                return rotateservo(board,PIN2,motorSol)
        elif komut=="00000005":
            motorSag += 10
            if motorSag >= 0 and motorSag <= 180:
                return rotateservo(board, PIN3,motorSag)
        elif komut=="00000006":
            motorSag -=10
            if motorSag >= 0 and motorSag <= 180:
                return rotateservo(board, PIN3,motorSag)
        elif komut=="00000007":
            motorPence +=10
            if motorPence >= 0 and motorPence <= 180:
                return rotateservo(board, PIN4,motorPence)
        elif komut=="00000010":
            motorPence -=10
            if motorPence >= 0 and motorPence <= 180:
                return rotateservo(board, PIN4, motorPence)
        else:
            print("Tanimli Degil")  

def main(): 
    
    board=Arduino(ARDUINO_PORT)
    
    board.digital[PIN1].mode=SERVO
    board.digital[PIN2].mode=SERVO
    board.digital[PIN3].mode=SERVO
    board.digital[PIN4].mode=SERVO
    
    sckt = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    sckt.bind((HOST,SOCKET_PORT))
    sckt.listen()

    t2 = threading.Thread(target=androidkomut, args=(sckt, board,))
    t2.start()

if __name__ == "__main__":
    main()