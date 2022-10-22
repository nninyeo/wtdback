package com.example.wtd.controller;

//.... 어쩌다가 주석용이 되어버림



//
//        HttpHeaders header = new HttpHeaders();
//        HttpEntity<?> entity = new HttpEntity<>(header);
////
//        //ResponseEntity<List<String>> resDetails = restTemplate.getForObject(detailURLs.get(0), new ParameterizedTypeReference<List<String>>() {});
//       ResponseEntity<List<String>> resDetails;
//            resDetails = restTemplate.exchange(detailURLs.get(0), HttpMethod.GET, entity, new ParameterizedTypeReference<List<String>>() {});
//       //ResponseEntity<List<String>> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, resultMap, 원하는클래스타입.class);
//
//        System.out.println(resDetails);
//





//        for(int i = 0; i < filterGetVO.getLocationList().size(); i++ ) {//todo: 하나만들어가는듯.
//            params.put("locations", filterGetVO.getLocationList().toString());
//            System.out.println("지역" + i + "번째: " + filterGetVO.getLocationList().get(i));
//        }
//        for(int i = 0; i < filterGetVO.getYearList().size(); i++ ){//todo: 하나만들어가는듯.
//            params.put("years", filterGetVO.getYearList().get(i));
//            System.out.println("년차" + i + "번째: " + filterGetVO.getYearList().get(i));
//        }



//restTemplate.execute() 이거 쿼리 붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고붙이고
//        restTemplate.getMessageConverters()
//                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));


//            UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url)
//                    .queryParam("country", country)
//                    .queryParam("job_sort", job_sort)
//                    .queryParam("locations", filterGetVO.getLocations())
//                    .queryParam("years", filterGetVO.getMaxyears())
//                    .queryParam("limit", filterGetVO.getLimit())
//                    .queryParam("offset", filterGetVO.getOffset());
//            //TODO: https:/www.wanted.co.kr/wdlist/518?country=country%3Dkr&job_sort=job.latest_order&locations=seoul.gangnam-gu&years&limit=20&offset=20
//            System.out.println(builder.toUriString());


//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//@RequestMapping("/api/test")
public class FirstController {

//    //test url: http://localhost:18080/api/main/jobs?name=test&age=22&add=321&age=22&nsdytg=321&sftg=22 ..... key value ...
//    @GetMapping(path= "jobs")
//    public String locationPara(@RequestParam Map<String, String> paraDatas){
//        StringBuilder sb = new StringBuilder();
//
//        paraDatas.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " = " + entry.getValue()+"\n");
//            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
//        });
//
//        String url = "https://www.wanted.co.kr/wdlist/518"; // api url
//
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/ppp")
//                .build()// {uid} 넣어줄 값
//                .toUri();
//        System.out.println(uri);
//
//        return sb.toString();







//        StringBuilder sb = new StringBuilder();
//
//        paraDatas.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " = " + entry.getValue()+"\n");
//            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
//        });
//
//       // String url = "https://www.wanted.co.kr/wdlist/518"; // api url
//
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/ppp")
//                .build()// {uid} 넣어줄 값
//                .toUri();
//        System.out.println(uri);

//        return sb.toString();



//        ParameterizedTypeReference<List<LocationResponse>> responseType = new ParameterizedTypeReference<List<LocationResponse>>() { };
//        ResponseEntity<List<LocationResponse>> responseEntity = restTemplate.exchange(targetUrl.toURL().toString(), HttpMethod.GET, entity, responseType);















//    }



}
