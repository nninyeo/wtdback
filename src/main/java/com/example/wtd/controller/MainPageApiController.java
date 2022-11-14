package com.example.wtd.controller;

import com.example.wtd.vo.DetailDataVO;
import com.example.wtd.vo.FilterGetVO;
import com.example.wtd.vo.MainDataVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3003")
@RestController
@RequestMapping("/api/main")
public class MainPageApiController {


    //@GetMapping(path= "jobs")
    @GetMapping(value = "/jobs")
    //@ResponseBody
    //public String locationPara(@RequestBody FilterGetVO filterGetVO) {
    public String locationPara(FilterGetVO filterGetVO) throws JsonProcessingException {

        /**************************************************************
         * A.사용자의 메인 게시글 GET요청온 params 처리 및 원티드 메인(공고페이지) GET URL생성
         *
         * GET요청 들어오는 URL 형식: http://localhost:18080/api/유저아이디&지역&년차&리밋&오프셋 (param은 순서없음)
         *   예> http://localhost:18080/api/main/jobs?country=kr&job_sort=job.latest_order&gyeonggi.hanam-si
         *
         * 재조합할 URL 형식: https://www.wanted.co.kr + uri + params
         *   예>  https://www.wanted.co.kr/api/v4/jobs?1665911121383&country=kr&tag_type_ids=518&job_sort=company.response_rate_order&locations=seoul.all&years=-1&limit=20&offset=20
         * params: 지역&년차&리밋&옵셋
         *   고정값: &country=kr&tag_type_ids=518
         *   지역: &locationList=seoul.all (서울전체), =seoul.gangnam-gu&locations=seoul.songpa-gu (강남구, 송파구) 최대 파라미터 개수: ?
         *   년차: &yearList=-1 (전체년차), =N&years=M (N~M년, 최소 0 최대 10)
         *   리밋: &limit=N (보여줄 글 갯수, 최소1 최대 100)
         *   옵셋: &offset=N (보여줄 글에 대한 총 수량 중 limit만큼 받아올 위치)
         *
         *   각 param의 null값에 대해서는 고려하지 않음
         **************************************************************/
       //A-1. 요청 params의 필터vo 매칭 확인
//        System.out.println("1.유저아이디" + filterGetVO.getUserId());
//        System.out.println("1.최소년차" + filterGetVO.getYearList());
//        System.out.println("1.희망지역" + filterGetVO.getLocationList());
//        System.out.println("1.리밋" + filterGetVO.getLimit());
//        System.out.println("1.오프셋" + filterGetVO.getOffset());

       //A-2. URL 재조합 - const 설정
        String url = "";
        String protocol = "https";
        String server = "www.wanted.co.kr";
        String uri = "/api/v4/jobs";

        //A-3. URL 재조합 - 변동값 조합
        Map<String, String> params = new HashMap<String, String>();
        //HashMap중복비허용, MultiValueMap로 전환?
        //MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //MultiValueMap 사용시 params.add 해야하나 쿼리스트링이 K,List<V>형태로 나오는 문제로인하여
        //강제로 List<String> -> String 으로 이어붙이기를 할 것이다. 코드가 너무 길어져서..
        //ref: https://jojoldu.tistory.com/478

        //A-3-1. year 파라미터 조합: MultiValueMap로<> 사용하기가 너무 코드가 길어져서 간단히 사용
        String years = "";
        StringBuilder buildYears = new StringBuilder();
        if (filterGetVO.getYearList().size() == 2){

            buildYears.append((filterGetVO.getYearList().get(0)));
            buildYears.append("&years=");
            buildYears.append((filterGetVO.getYearList().get(1)));
            years = buildYears.toString();
        }else{
            years = "years=-1";
        }

        //A-3-2. Location 파라미터 조합: MultiValueMap로<> 사용하기가 너무 코드가 길어져서 간단히 사용
        List<String> locTemp = new ArrayList<>();
        for(int i = 0; i < filterGetVO.getLocationList().size(); i++ ){
            locTemp.add( filterGetVO.getLocationList().get(i) );
        }

        String joinedLocation = "";
        StringBuilder buildLoc = new StringBuilder("");
        for(int i = 0; i < filterGetVO.getLocationList().size(); i++ ) {
            buildLoc.append(locTemp.get(i));
            if(filterGetVO.getLocationList().size() == (i+1)) break;
            buildLoc.append("&locations=");
        }
        joinedLocation = buildLoc.toString();

        //A-3-3. 파라미터 조합
        params.put("country", "kr");
        params.put("job_sort", "job.latest_order");
        params.put("locations", joinedLocation);
        params.put("years", years);
        params.put("limit", filterGetVO.getLimit());
        params.put("offset", filterGetVO.getOffset());

        //A-4. URL params의 null 처리 (이부분 코드는 생략해도 되는지 확인바람)
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap queryParams = new LinkedMultiValueMap<>();
        queryParams.setAll(
                objectMapper.convertValue(
                        (
                                (params == null) ? new HashMap<>() : params), Map.class)
        );

        //A-5. URL 조립
        url = UriComponentsBuilder.newInstance()
                .scheme(protocol)
                .host(server)
                .path(uri)
                .queryParams(queryParams)
                .build()
                .toUriString();

        System.out.println("조합된url: " + url);
        System.out.println("유저ID: " + filterGetVO.getUserId());


        /** B.원티드서버에 GET요청 및 메인vo에 매칭: 메인게시글들 내용
         * */

        //메인페이지 요청 (레스트템플릿 이용) - 공고 3건
        //String baseUrl = "https://www.wanted.co.kr/api/v4/jobs?1666036191510&country=kr&tag_type_ids=518&job_sort=company.response_rate_order&locations=seoul.gangnam-gu&locations=seoul.gangdong-gu&locations=seoul.guro-gu&years=1&years=9&limit=3&offset=20";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resMain = restTemplate.getForEntity(url, String.class);
        System.out.println("메인데이터:" + resMain.getBody());  //resMain = 전체받은 결과, +.getBody() = 그 중 Body만 떼옴

        Gson gson = new Gson();

        MainDataVO mainDataVO = new MainDataVO(); //vo객체를 만들고 (vo안쓰면 id뽑기 애매함)
        mainDataVO = gson.fromJson(resMain.getBody(), MainDataVO.class);  //매칭: 객체에다가 json형태 데이터를 (어떤걸, 어디에) 변환해서 vo객체에 넣었다. 근데 data 필드가 있어서 data{}해당하는것들이 거기로들어감.


        /** C. 메인게시글 데이터에서 ID 추출 */


        //VO에 들어간 data[]을 리스트맵으로 변환: VO내용물이 json형태를 대충 갖춘 obj data라서 바로 못넣는다. 형변환을해줌.
        List<Map<String, Object>> wtdMainDataList = (List<Map<String, Object>>) (Object) mainDataVO.getData();

        List<String> mainId = new ArrayList<String>();
        String id = ""; //힙메모리 많이 먹어서 for위로 뺏음, 임시변수

        //돌면서 data[]에서 id를 빼서 mainId에 add
        for(int i = 0; i < wtdMainDataList.size(); i++) {
            id = String.format("%.0f", wtdMainDataList.get(i).get("id")); //ID를 추출후 스트링 변환해서 저장반복
            mainId.add(id);
        }//원래 D가 반복문 안에있어야하는데 테스트하려고 분리.. 햇갈리기도함

        System.out.println(mainId.toString());

        List<String> detailURLs = new ArrayList<String>();
//
//        for(int i = 0; i < mainId.size(); i++) {
//
//        }

        //최종 완성될 JSONObject
        JSONObject jsonObjectResult = new JSONObject();
        //상세페이지 JSON정보를 담을 Array
        JSONArray detailInfos = new JSONArray();

        for(int i = 0; i < mainId.size(); i++) {


            /** D. 추출한 ID로 상세페이지 URL조합
             *    예> Request URL: https://www.wanted.co.kr/api/v4/jobs/82531?1666387270125
             *        url: https://www.wanted.co.kr (고정)
             *        uri: api/v4/jobs
             *        param: 게시물ID(필수) + 시간(옵션)
             * */

            detailURLs.add("https://www.wanted.co.kr/api/v4/jobs/" + mainId.get(i));
            System.out.println(detailURLs.get(i));


            /** E. 원티드서버에 GET요청: 추출한 ID로 상세페이지 개별요청 */

            ResponseEntity<String> resDetail = restTemplate.getForEntity(detailURLs.get(i), String.class);
            DetailDataVO detailDataVO = new DetailDataVO();
            JsonObject jsonObject = gson.fromJson(resDetail.getBody(), JsonObject.class);
            //System.out.println("상세데이터.body:" + resDetail.getBody());


            /** F. 상세페이지 내용에서 depth있는것들을 재 매칭 후 필요한 요소들 추출 */

            DetailDataVO.JobDepth voJobDepth = new DetailDataVO.JobDepth();

            //JsonObj->vo : job 안에서 depth가 있는 것들은 voJobDepth 객체안에 넣을것이다.
            voJobDepth = gson.fromJson(jsonObject.get("job"), DetailDataVO.JobDepth.class);

            //job.addres 오브젝트 -> String으로 변환 : vo에 매칭하기위함함
            ObjectMapper mapper = new ObjectMapper();
            String jobAddrData = mapper.writeValueAsString(voJobDepth.getAddress());
            String jobCompData = mapper.writeValueAsString(voJobDepth.getCompany());
            String jobImgData = mapper.writeValueAsString(voJobDepth.getTitle_img());
            String jobDetailData = mapper.writeValueAsString(voJobDepth.getDetail());
            String jobPosData= mapper.writeValueAsString(voJobDepth.getPosition());
            String jobDueTimeData= mapper.writeValueAsString(voJobDepth.getDue_time()); //null = 상시
            if(voJobDepth.getDue_time() == null){
                jobDueTimeData = "상시";
            }
            String jobStatusData= mapper.writeValueAsString(voJobDepth.getStatus()); //draft: 지원마감 (active아니면 다 마감)

            //String으로 변환된 주소모음(jobAddrData)을 depth타고 올라가서 해당 vo에 매칭
            DetailDataVO.AddressData addressData = gson.fromJson(jobAddrData, DetailDataVO.AddressData.class);
            DetailDataVO.CompanyData companyData = gson.fromJson(jobCompData, DetailDataVO.CompanyData.class);
            DetailDataVO.TitleImgData titleImgData = gson.fromJson(jobImgData, DetailDataVO.TitleImgData.class);
            DetailDataVO.DetailData detailData = gson.fromJson(jobDetailData, DetailDataVO.DetailData.class);

//            (addressData.getGeo_location() != null){
            String location = "";
            try {
                location = addressData.getGeo_location().get("location").toString();
            }catch (Exception e) {
                System.out.println("위도경도값 null");
//                location = "{lat=-1, lng=-1}";
                continue;
            }


            String dueTime = voJobDepth.getDue_time();
            String statusData = voJobDepth.getStatus();
            if(voJobDepth.getStatus().contains("active")){
                statusData = "모집중";
            }else {
                statusData = "마감";
            }
            
            String positionData = voJobDepth.getPosition();


            System.out.println("회사이름   >>>" + companyData.getName());
            System.out.println("회사주소   >>>" + addressData.getFull_location());
            System.out.println("썸네일     >>>" + titleImgData.getThumb());
            System.out.println("게시물ID   >>>" + mainId.get((i)));
            System.out.println("마커좌표   >>>" + location);
            System.out.println("상세정보   >>>" + detailData.getMain_tasks());
            System.out.println("마감일     >>>" + jobDueTimeData);
            System.out.println("지원가능여부>>>" + statusData);
            System.out.println("글제목     >>>" + positionData);
            


            /** G. 매칭된 요소에서 필요한 요소들 List화 */

            //1개의 상세페이지 내용이 들어갈 JSONObject
            JSONObject detailInfo = new JSONObject();

            detailInfo.put("id", mainId.get((i)));
            detailInfo.put("company", companyData.getName());
            detailInfo.put("position", positionData);
            detailInfo.put("location", location);
            detailInfo.put("status", statusData);
            detailInfo.put("dueTime", jobDueTimeData);
            detailInfo.put("thumb", titleImgData.getThumb());
            detailInfo.put("address", addressData.getFull_location());
            detailInfo.put("detailRequirements", detailData.getRequirements());
            detailInfo.put("detailMain_tasks", detailData.getMain_tasks());
            detailInfo.put("detailIntro", detailData.getIntro());
            detailInfo.put("detailBenefits", detailData.getBenefits());
            detailInfo.put("detailPreferred_points", detailData.getPreferred_points());

            System.out.println(detailInfo);
            detailInfos.add(detailInfo);

        }


        /** G. 리액트로 리턴
         * 리턴 형식:
         * data: [{{address}{company}{detailBenefits}{detailIntro}{detailMain_tasks}{detailPreferred_points}{detailRequirements}
         *         {dueTime}{id}{location}{position}{status}{thumb}}  {...} {...}]
         * header: 미정
         * */

        jsonObjectResult.put("data", detailInfos);
        //JSONObject를 String 객체에 할당
        String jsonInfo = jsonObjectResult.toJSONString();
        System.out.print(jsonInfo);

        return jsonInfo;
    }//~

//  https://developers.google.com/maps/documentation/geocoding/cloud-setup 차후 키 받아서 진행
//    public static String findGeoPoint(String location) {
//
//        if (location == null){
//            System.out.println("널됨1");
//            return null;
//        }
//
//
//        // setAddress : 변환하려는 주소 (경기도 성남시 분당구 등)
//        // setLanguate : 인코딩 설정
//        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("ko").getGeocoderRequest();
//
//        try {
//            Geocoder geocoder = new Geocoder();
//            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
//
//            if (geocoderResponse.getStatus() == GeocoderStatus.OK & !geocoderResponse.getResults().isEmpty()) {
//                GeocoderResult geocoderResult=geocoderResponse.getResults().iterator().next();
//                LatLng latitudeLongitude = geocoderResult.getGeometry().getLocation();
//
//                String coords = "";
////                coords =
//                coords = latitudeLongitude.getLat().toString();//floatValue();
//                System.out.println(coords);
////                coords[1] = latitudeLongitude.getLng().toString();//floatValue();
//
//            return coords;
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        System.out.println("널됨2");
//        return null;
//    }


}