import cv2
import numpy as np


# itembox클래스
class itemBox:
    # 이름
    name = "IB"
    # 식별이미지
    discernImg = None
    # 출고상태
    stockState = 0
    # 측정무게
    weight = 0.00

    # 기본생성자
    def __init__(self, imageUrl, _name):
        self.discernImg = imageUrl
        self.name = _name
        # cv2.imshow("T",discernImg)
        # discernImg.show()
        # print(discernImg)
        print("객체 생성 완료")

    def scanImge(self, imageUrl, _name):
        name = _name

    # 이미지 보기
    def showingImage(self):
        cv2.imshow("savedImage", self.discernImg)


# 유사 이미지 검색

# img1 = cv2.imread('./after1.png')
# img2 = cv2.imread('./after2.png')
# img3 = cv2.imread('./after3.png')
# img4 = cv2.imread('./after4.png')
# model =cv2.imread('./after4.png')
# imgs = [img1, img2, img3, img4]
# 모델은 비교 이미지,imgs는 기존 배열 동일한게 있으면 번호 리턴 일치하는거없으면 -1리턴
def similarNum(model, imgs):
    hsvM = cv2.cvtColor(model, cv2.COLOR_BGR2HSV)
    histM = cv2.calcHist([hsvM], [0, 1], None, [180, 256], [0, 180, 0, 256])
    cv2.normalize(histM, histM, 0, 1, cv2.NORM_MINMAX)

    hists = []
    for i, img in enumerate(imgs):
        # plt.subplot(1,len(imgs),i+1)
        # plt.title('img%d'% (i+1))
        # plt.axis('off')
        # plt.imshow(img[:,:,::-1])
        # ---① 각 이미지를 HSV로 변환
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        # ---② H,S 채널에 대한 히스토그램 계산
        hist = cv2.calcHist([hsv], [0, 1], None, [180, 256], [0, 180, 0, 256])
        # ---③ 0~1로 정규화
        cv2.normalize(hist, hist, 0, 1, cv2.NORM_MINMAX)
        hists.append(hist)
    # cv2.imshow("t",model)

    result = cv2.compareHist(histM, hists[0], cv2.HISTCMP_BHATTACHARYYA)
    print(result)
    print("---------------")

    for idx, val in enumerate(hists):
        # 모델도 히스토그램이어야한다
        result = cv2.compareHist(histM, val, cv2.HISTCMP_BHATTACHARYYA)
        print(result)
        if (result <= 0.3):
            return idx
        else:
            print(str(idx) + "일치하지않음")
    return -1


# 입고된 제품 이미지
ArrayStock = []

# 카메라 화면 연결및 조정
cap = cv2.VideoCapture(0)
frame_size = (int(cap.get(cv2.CAP_PROP_FRAME_WIDTH)),
              int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)))
print('frame_size=', frame_size)

# 이미지 배열
ArrayImage = []
# 박스가 시작 종료될때 위치저장 해서 박스 인식
startBoxpositon = 0
endBoxposition = 0


# 원하는 부분 자르기 함수 정의
def cropMarker(boxSize):
    # 크기로 판단하자
    if boxSize.ravel()[4] - boxSize.ravel()[0] >= 50 and boxSize.ravel()[7] - boxSize.ravel()[3] >= 50:
        width = boxSize.ravel()[4] - boxSize.ravel()[0]
        startH = boxSize.ravel()[3]
        height = boxSize.ravel()[7] - boxSize.ravel()[3]
        crop_img = img[startH:startH + height, x:x + width]
        cv2.imshow("cropped", crop_img)
        ArrayImage.append(crop_img)

        print("인식완료")


# 가장 선명한 이미지 검출
def clearImageSerch(imageList):
    # 선명도 숫자
    bestClearImageV = 0
    # 선명한 이미지 번호
    bestClearImageN = 0

    # 선명도를 저장하고 더 높은게 있으면V N업데이트

    for i in range(len(imageList)):
        img = imageList[i]
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        value = cv2.Laplacian(gray, cv2.CV_64F).var()
        if bestClearImageV == 0 or value > bestClearImageV:
            bestClearImageN = i
            bestClearImageV = value

    print("선명한이미지탐색완료")
    print(bestClearImageV)
    cv2.imshow("imagelist[bestClearImageN]", ArrayImage[bestClearImageN])


# 영상 송출( 보여주기 위함 실제 작동필요x)
while True:
    retval, frame = cap.read()  # 프레임캡처
    if not retval:
        break

    # 펜타곤 검출
    img = frame
    # 테스트 이미
    # img = cv2.imread('Image1.png')
    imgGrey = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    _, thrash = cv2.threshold(imgGrey, 240, 255, cv2.THRESH_BINARY)
    contours, _ = cv2.findContours(thrash, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    # 비교이미지
    cv2.imshow("img", img)
    # 검출이미지 찾기
    for contour in contours:
        # 두번째 인자 크기 작으면  정확도높아짐
        approx = cv2.approxPolyDP(contour, 0.05 * cv2.arcLength(contour, True), True)
        # 보기위해서 그림그려주는
        # cv2.drawContours(img, [approx], 0, (0, 0, 0), 5)
        x = approx.ravel()[0]
        y = approx.ravel()[1] - 50
        # 5개의 점이 인식 되었을때
        if len(approx) == 5:
            cropMarker(approx)

            # 캡
            cv2.putText(img, "ItemBoxMarker", (x, y), cv2.FONT_HERSHEY_COMPLEX, 0.5, (0, 0, 0))

    # 검출된 이미
    cv2.imshow('test', img)

    key = cv2.waitKey(25)
    if key == 27:
        break
if cap.isOpened():
    cap.release()
cv2.destroyAllWindows()

cv2.imshow("array", ArrayImage[1])
print(len(ArrayImage))
clearImageSerch(ArrayImage)
# 다른거



