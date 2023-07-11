# 🚀출동, 팻맨! 안드로이드
### 안드로이드 개발 환경
 - Languages : Kotlin
 - Tool : Android Studio, Git, Notion
    - Android Studio : Flamingo
    - java : Java 8
    - Android Gradle Plugin : 8.0.1
    - Gradle Version : 8.0
 - - -
### 사용된 오픈소스 라이브러리 목록
| 이름 | 라이센스 |
|---|---|
|[SceneView 0.10.0](https://github.com/SceneView/sceneview-android) | Apache License 2.0 |
|[Glide](https://github.com/bumptech/glide) | BSD-2, part MIT and Apache 2.0 [(상세)](https://github.com/bumptech/glide/blob/master/LICENSE) |
|[CircleImageView](https://github.com/hdodenhof/CircleImageView) | Apache License 2.0 |
|[Retrofit2](https://square.github.io/retrofit/) | Apache License 2.0 |
- - -
### 사용된 API 목록
| 이름 | 설명 |
|---|---|
| Google Maps SDK for Android | 구글 지도를 활용하여 지도 기능을 구현
| 백엔드 API | 비즈니스 로직 구현을 위한 API **아직 앱에 도입하지 않았고 예정임을 알려드립니다.* |

- - -
### Git 사용 관련 안내사항
1. push를 하기 전에 현재 branch가 push를 해야 하는 branch인지 pull이 제대로 된 상태인지 확인해주세요.
2. branch를 새로 생성할 땐 최대한 main branch를 기점으로 생성해주세요.
3. 원활한 작업을 위해서 [Commit Message Convention](https://www.conventionalcommits.org/en/v1.0.0/)을 도입해볼까 합니다. Git의 tag 기능을 사용해서 어떤 작업을 했는지 구분해주세요. 
4. commit message를 아래와 같이 작성해주세요.
    tag_name : 작업 내용 #issues 번호(issues 해결 혹은 발생 시)
    *ex)Fix : MapFragment 비정상 종료 해결 #27*, *Feat : ArActiviy에서 이전 화면으로 돌아가는 기능 #3*, *Rename : fragment, activity package 구분*
    **issue는 해야 하는 작업, 버그 수정, 개선 사항, 새로 추가될 기능 등등 구분 되어야 할 것 같을 때 생성해주시면 됩니다.*
- - -
### tag_name 목록
| tag_name | 설명 |
|---|---|
| Feat | 새로운 기능 추가 |
| Add | 파일 추가 |
| Fix | 버그 수정 |
| Design | UI 변경 혹은 UI 작업 |
| !BREAKING CHANGE | API 변경으로 인해 수정이 필요한 경우 |
| !HOTFIX | 빠르게 치명적인 버그를 고쳐야 하는 경우 |
| Comment | 주석 작업(추가 및 변경) |
| Refactor | 코드 리팩토링(최적화) |
| Test | 테스트 코드 추가(실제 코드 변경이 없을 때) |
| Docs | 문서 수정
| Rename | 파일 혹은 폴더명을 수정하거나 옮기는 작업만 하는 경우 |
| Remove | 파일을 삭제하는 작업만 수행한 경우 |
