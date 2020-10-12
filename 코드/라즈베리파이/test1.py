import cv2
import numpy as np

#카메라 화면 연결및 조정
cap =cv2.VideoCapture(0)
frame_size=(int(cap.get(cv2.CAP_PROP_FRAME_WIDTH)),
            int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)))
print('frame_size=',frame_size)


#이미지 배열
ArrayImage =[]
#박스가 시작 종료될때 위치저장 해서 박스 인식
startBoxpositon =0
endBoxposition= 0


#원하는 부분 자르기 함수 정의
def cropMarker(boxSize):
    #크기로 판단하자
    if boxSize.ravel()[4]-boxSize.ravel()[0]>=50 and boxSize.ravel()[7]-boxSize.ravel()[3]>=50:
        width = boxSize.ravel()[4]-boxSize.ravel()[0]
        startH=boxSize.ravel()[3]
        height= boxSize.ravel()[7]-boxSize.ravel()[3]
        crop_img = img[startH:startH+height, x:x+width]
        cv2.imshow("cropped", crop_img)
        ArrayImage.append(crop_img)
        
        print("인식완료")

#가장 선명한 이미지 검출
def clearImageSerch(imageList):
    img=imageList[1]
    gray =cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    value=cv2.Laplacian(gray,cv2.cv_64F).var()
    print("선명도 ="+value)

#영상 송출( 보여주기 위함 실제 작동필요x)
while True:
    retval, frame =cap.read()#프레임캡처
    if not retval:
        break


    #펜타곤 검출
    img= frame
    #테스트 이미
    #img = cv2.imread('Image1.png')
    imgGrey =cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    _, thrash = cv2.threshold(imgGrey, 240, 255, cv2.THRESH_BINARY)
    contours, _ = cv2.findContours(thrash, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    #비교이미지
    cv2.imshow("img", img)
    #검출이미지 찾기
    for contour in contours:
        #두번째 인자 크기 작으면  정확도높아짐
        approx = cv2.approxPolyDP(contour, 0.05* cv2.arcLength(contour, True), True)
        cv2.drawContours(img, [approx], 0, (0, 0, 0), 5)
        x = approx.ravel()[0]
        y = approx.ravel()[1] - 50
        #5개의 점이 인식 되었을때
        if len(approx) == 5:
            cropMarker(approx)



            #캡
            cv2.putText(img, "ItemBoxMarker", (x, y), cv2.FONT_HERSHEY_COMPLEX, 0.5, (0, 0, 0))
        
    #검출된 이미
    cv2.imshow('test',img)

    key =cv2.waitKey(25)
    if key ==27:
        break
if cap.isOpened():

    
        cap.release()
cv2.destroyAllWindows()


cv2.imshow("array",ArrayImage[1])
print(len(ArrayImage))
clearImageSerch(ArrayImage)
#다른거


    
