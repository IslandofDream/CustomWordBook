# **◎ 앱 개발 방향**

 해당 어플은 영어단어장입니다. 영어단어장은 기능들이 많지 않기 때문에 여러개의 액티비티가 아닌 하나의 액티비티에 여러개의 Fragment를 활용하여서 액티비티를 통해 제어할 것입니다. 이를 통해서 화면이동간의 자원 이용 및 어플의 속도를 보장 할 것입니다. 

 또한 기본적인 영어단어들을 저장해 두어야 하기 때문에 영어단어 자료들이 필요합니다. 다만, 파일입출력 방식이 아닌 데이터베이스를 활용할 계획입니다. 다만, 온라인 연결 없이도 오프라인만으로도 사용이 가능하게끔 내부 데이터베이스인 mysql을 사용하여 내장 데이터베이스를 통해서 영어단어들을 관리 할 것입니다. 

 나만의 영어 단어장이라는 취지에 맞게 기본적으로 제공하는 영단어 이외에도 사용자가 직접 자신만의 영어단어를 직접 입력하고 저장 할 수 있습니다. 이는 위에서 말한 어플 내장 데이터베이스에도 저장되어서 사용자가 원하는 대로 자신만의 영어 단어장을 만들 수 있습니다.

 다른 영어단어장과는 다르게 학습 동기부여를 위한 요소들도 일부 있습니다. 뜻 혹은 음에 따른 객관식 퀴즈기능 또한 구현하였습니다.

 전문성을 위해서 파싱도 구현할 것입니다. 사용자가 알기 원하는 영어의 음, 뜻을 텍스트 입력창에 입력하면, 영어사전으로 파싱하여 따로 웹 브라우저에 접속하지 않아도 사용자가 원하는 영단어의 음,뜻을 알 수 있도록 하여서 전문성을 보충 하였습니다.

# **◎ 앱 구성**

## **<메인 액티비티>**

메인 액티비티는 다음과 같습니다. 하나의 액티비티속에 Fragment Container를 삽입하여서 가장 하단의 메뉴들을 선택하면 Viewpager를 통해서 Fragment를 전환합니다. Fragment가 변화함에 따라서 가장 상단 텍스튜의 이름이 변화하면서 어떤 내용의 Fragment인지 알 수 있습니다.

![image02](https://user-images.githubusercontent.com/70648111/149072093-3e5825fb-7440-46db-8407-9036e9a5c032.png)

## **<데이터베이스>**

데이터베이스의 구성은 다음과 같습니다. 총 4개의 애티리뷰트가 있으며, 각각 id,영단어, 뜻, 이후 북마크를 활용하기 위한 checked 애트리뷰트가 존재합니다.

![Untitled](https://user-images.githubusercontent.com/70648111/149071869-0f72a999-dccc-4b58-a034-5a3906df8f69.png)

## **<단어장>**

다음은 단어장 Fragment입니다. 다음과 같이 상단에는 검색을 위한 edittext와 전제조회 버튼 , 검색 버튼이 있습니다.

단어들은 리싸이클러뷰를 통해서 데이터베이스의 단어들을 리스트를 통해서 출력합니다.

우측에는 별표형태의 토글버튼이 있습니다. 별이 채워지면 on이고 비워지면 off입니다. 위에서 설명한 데이터베이스에서의 checked 애트리뷰트에 따라서 토글버튼이 on/off되면서 내가 원하는 단어들을 즐겨찾기에 등록이 가능합니다. 

일부 단어는 임의로 삭제가능합니다. 

![212](https://user-images.githubusercontent.com/70648111/149071821-29debe44-46bf-42ee-9af3-af981b1ce425.gif)

![4](https://user-images.githubusercontent.com/70648111/149071764-58d2652d-7393-40bd-8e4b-bd5661d7b6f7.gif)

## **<즐겨찾기>**

다음은 즐겨찾기 Fragment입니다. 다음과 같이 우측의 토글버튼이 모두 on이 된 것을 볼 수 있습니다.

checked 애트리뷰트가 모두 1인 상태이기 때문에 모든 토글버튼이 on 상태입니다. 해당 Fragment에서도 토글버튼을 통해서 즐겨찾기 해제가 가능합니다.

![3_1](https://user-images.githubusercontent.com/70648111/149071829-df8443c9-1d75-4526-aab2-6e23c46a972b.gif)

![5](https://user-images.githubusercontent.com/70648111/149071991-1ad180db-1c15-4fd4-9869-970df574d9a2.gif)

## **<퀴즈>**

다음은 퀴즈 Fragment입니다. 객관식 퀴즈 형태로 단어퀴즈를 구성하였습니다.

Fragment 내부에 별도의 Viewpager를 하나 더 두어서 영단어를 보고 단어를 해석 할지 혹은 한국어 해석을 보고 영단어를 고를지 선택하는 형태입니다.

4지 선다형중 하나를 선택하면 토스트메세지와 중앙의 이모티콘을 통해서 결과를 알 수 있으며

해당 퀴즈 프래그먼트에서도 단어의 즐겨찾기를 등록 할 수 있습니다. 힌트와 정답공개를 통해서 문제풀이에 도움을 받을 수도 있습니다.

![6](https://user-images.githubusercontent.com/70648111/149071783-afef1916-f0e9-4a82-b8af-42cf13b686a5.gif)

![7](https://user-images.githubusercontent.com/70648111/149071791-19b11de9-c065-44cf-a165-94f84c191140.gif)

## **<검색 및 저장>**

마지막은 검색 및 저장 Fragment입니다. 다음과 같이 영단어, 한국어인 뜻을 입력하는 인풋 텍스트가 각각 두 개 위치해 있습니다. 단어와 뜻을 모두 입력하고 추가 및 수정 버튼을 누르게 되면, 단어장에 단어를 추가 할 수 있습니다.

단어 edittext 혹은 뜻 edittext 둘중 하나만 입력하고 검색 버튼을 누르게 되면 다음 영어사전으로부터 검색결과를 파싱해와서 비워둔 edittext에 검색결과를 보여줍니다. 검색결과를 자세히 보고 싶으면 Daum 사전이동 버튼을 통해서 해당 페이지로 이동 할 수도 있습니다,

### 파싱

![9](https://user-images.githubusercontent.com/70648111/149071814-c7966c3c-30ac-4fab-9ae6-98a6bcc9dbf9.gif)

### 단어 저장

![8](https://user-images.githubusercontent.com/70648111/149071808-34df3f33-d511-4763-922d-3431e8e470cd.gif)

# **◎ 느낀점 및 개선점**

리싸이클러뷰와 데이터베이스간의 연결을 하면서 생각 할 점이 많았습니다. 데이터베이스로부터 맞아온 데이터형식을 직접 리싸이클러뷰의 어댑터에 넣는 과정에서 고려해야할 점이 많았기 때문에 데이터베이스의 데이터 정규화의 중여성을 느겼습니다. 또한 프래그먼트간의 유용성과 프래그먼트와 액티비티간의 차이점을 어느정도 배우게 되었습니다.

파싱을 하면서도 예상치 못한 결과가 나왔을 때 예외사항에 대한 파싱이 다소 미흡했고 파싱 결과의 출력 및 방식이 다소 직관적이지 않다는 점이 아쉬웠습니다.
