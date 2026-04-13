create sequence scs_hosp_device_hosp_device_id_seq
    start with 100000;

alter sequence scs_hosp_device_hosp_device_id_seq owner to ailis_user;

create sequence scs_hosp_medi_sbjt_hosp_medi_sbjt_id_seq
    start with 500000;

alter sequence scs_hosp_medi_sbjt_hosp_medi_sbjt_id_seq owner to ailis_user;

create sequence scs_req_rst_if_method_req_rst_if_method_id_seq;

alter sequence scs_req_rst_if_method_req_rst_if_method_id_seq owner to ailis_user;

create sequence scs_cust_tst_cd_mpg_cust_tst_cd_mpg_id_seq;

alter sequence scs_cust_tst_cd_mpg_cust_tst_cd_mpg_id_seq owner to ailis_user;

create sequence scs_cust_charge_cust_charge_id_seq;

alter sequence scs_cust_charge_cust_charge_id_seq owner to ailis_user;

create sequence scs_appr_info_appr_info_no_seq
    start with 3;

alter sequence scs_appr_info_appr_info_no_seq owner to ailis_user;

create table scs_hosp_mst
(
    care_inst_id      varchar(50)          not null
        constraint "PK_scs_hosp_mst"
            primary key,
    encp_care_inst_no varchar(100)         not null,
    care_inst_no      varchar(50),
    care_inst_nm      varchar(500)         not null,
    asrt_cd           varchar(50),
    estb_div_nm       varchar(100),
    sido_cd           varchar(50),
    sido_nm           varchar(100),
    sggu_cd           varchar(50),
    sggu_nm           varchar(100),
    emd               varchar(50),
    zipcd             varchar(10),
    addr              varchar(500),
    telno             varchar(50),
    hp_url            varchar(1000),
    open_dt           varchar(8),
    close_dt          varchar(8),
    dr_cnt            integer,
    sickbed_cnt       integer,
    map_codn_x        double precision,
    map_codn_y        double precision,
    use_yn            boolean default true not null,
    creator           varchar(50)          not null,
    create_dtime      timestamp            not null,
    updater           varchar(50)          not null,
    update_dtime      timestamp            not null
);

comment on table scs_hosp_mst is '병원';

comment on column scs_hosp_mst.care_inst_id is '요양기관아이디';

comment on constraint "PK_scs_hosp_mst" on scs_hosp_mst is '병원 기본키';

comment on column scs_hosp_mst.encp_care_inst_no is '암호화요양기관번호';

comment on column scs_hosp_mst.care_inst_no is '요양기관번호';

comment on column scs_hosp_mst.care_inst_nm is '요양기관명';

comment on column scs_hosp_mst.asrt_cd is '종별코드';

comment on column scs_hosp_mst.estb_div_nm is '설립구분명';

comment on column scs_hosp_mst.sido_cd is '시도코드';

comment on column scs_hosp_mst.sido_nm is '시도명';

comment on column scs_hosp_mst.sggu_cd is '시군구코드';

comment on column scs_hosp_mst.sggu_nm is '시군구명';

comment on column scs_hosp_mst.emd is '읍면동';

comment on column scs_hosp_mst.zipcd is '우편번호';

comment on column scs_hosp_mst.addr is '주소';

comment on column scs_hosp_mst.telno is '전화번호';

comment on column scs_hosp_mst.hp_url is '홈페이지URL';

comment on column scs_hosp_mst.open_dt is '개설일자';

comment on column scs_hosp_mst.close_dt is '폐쇄일자';

comment on column scs_hosp_mst.dr_cnt is '의사수(건수)';

comment on column scs_hosp_mst.sickbed_cnt is '병상수(건수)';

comment on column scs_hosp_mst.map_codn_x is '지도좌표x';

comment on column scs_hosp_mst.map_codn_y is '지도좌표y';

comment on column scs_hosp_mst.use_yn is '사용여부';

comment on column scs_hosp_mst.creator is '생성자';

comment on column scs_hosp_mst.create_dtime is '생성일시';

comment on column scs_hosp_mst.updater is '수정자';

comment on column scs_hosp_mst.update_dtime is '수정일시';

alter table scs_hosp_mst
    owner to ailis_user;

create table scs_hosp_device
(
    hosp_device_id bigint  default nextval('sales_scm.scs_hosp_device_hosp_device_id_seq'::regclass) not null
        constraint "PK_scs_hosp_device"
            primary key,
    care_inst_id   varchar(50)
        constraint "FK_scs_hosp_mst_TO_scs_hosp_device"
            references scs_hosp_mst,
    device_cd      varchar(50)                                                                       not null,
    device_nm      varchar(200),
    device_cnt     integer,
    use_yn         boolean default true                                                              not null,
    creator        varchar(50)                                                                       not null,
    create_dtime   timestamp                                                                         not null,
    updater        varchar(50)                                                                       not null,
    update_dtime   timestamp                                                                         not null
);

comment on table scs_hosp_device is '병원장비';

comment on column scs_hosp_device.hosp_device_id is '병원장비아이디';

comment on constraint "PK_scs_hosp_device" on scs_hosp_device is '병원장비 기본키';

comment on column scs_hosp_device.care_inst_id is '요양기관아이디';

comment on constraint "FK_scs_hosp_mst_TO_scs_hosp_device" on scs_hosp_device is '병원 -> 병원장비';

comment on column scs_hosp_device.device_cd is '장비코드';

comment on column scs_hosp_device.device_nm is '장비명';

comment on column scs_hosp_device.device_cnt is '장비수(건수)';

comment on column scs_hosp_device.use_yn is '사용여부';

comment on column scs_hosp_device.creator is '생성자';

comment on column scs_hosp_device.create_dtime is '생성일시';

comment on column scs_hosp_device.updater is '수정자';

comment on column scs_hosp_device.update_dtime is '수정일시';

alter table scs_hosp_device
    owner to ailis_user;

alter sequence scs_hosp_device_hosp_device_id_seq owned by scs_hosp_device.hosp_device_id;

create table scs_hosp_medi_sbjt
(
    hosp_medi_sbjt_id  bigint  default nextval('sales_scm.scs_hosp_medi_sbjt_hosp_medi_sbjt_id_seq'::regclass) not null
        constraint "PK_scs_hosp_medi_sbjt"
            primary key,
    care_inst_id       varchar(50)
        constraint "FK_scs_hosp_mst_TO_scs_hosp_medi_sbjt"
            references scs_hosp_mst,
    medi_sbjt_cd       varchar(50)                                                                             not null,
    medi_sbjt_nm       varchar(100),
    medi_sbjt_mdsp_cnt integer,
    selcare_dr_cnt     integer,
    use_yn             boolean default true                                                                    not null,
    creator            varchar(50)                                                                             not null,
    create_dtime       timestamp                                                                               not null,
    updater            varchar(50)                                                                             not null,
    update_dtime       timestamp                                                                               not null
);

comment on table scs_hosp_medi_sbjt is '진료과목';

comment on column scs_hosp_medi_sbjt.hosp_medi_sbjt_id is '병원의료(진료)과목아이디';

comment on constraint "PK_scs_hosp_medi_sbjt" on scs_hosp_medi_sbjt is '진료과목 기본키';

comment on column scs_hosp_medi_sbjt.care_inst_id is '요양기관아이디';

comment on constraint "FK_scs_hosp_mst_TO_scs_hosp_medi_sbjt" on scs_hosp_medi_sbjt is '병원 -> 진료과목';

comment on column scs_hosp_medi_sbjt.medi_sbjt_cd is '의료(진료)과목코드';

comment on column scs_hosp_medi_sbjt.medi_sbjt_nm is '의료(진료)과목명';

comment on column scs_hosp_medi_sbjt.medi_sbjt_mdsp_cnt is '의료(진료)과목전문의수(건수)';

comment on column scs_hosp_medi_sbjt.selcare_dr_cnt is '선택진료의사수(건수)';

comment on column scs_hosp_medi_sbjt.use_yn is '사용여부';

comment on column scs_hosp_medi_sbjt.creator is '생성자';

comment on column scs_hosp_medi_sbjt.create_dtime is '생성일시';

comment on column scs_hosp_medi_sbjt.updater is '수정자';

comment on column scs_hosp_medi_sbjt.update_dtime is '수정일시';

alter table scs_hosp_medi_sbjt
    owner to ailis_user;

alter sequence scs_hosp_medi_sbjt_hosp_medi_sbjt_id_seq owned by scs_hosp_medi_sbjt.hosp_medi_sbjt_id;

create table scs_cust_add_info
(
    cust_add_info_id bigserial
        constraint "PK_scs_cust_add_info"
            primary key,
    cust_mst_id      varchar(50)          not null,
    cust_cd          varchar(50)          not null,
    spnote_div_cd    varchar(50)          not null,
    spnote           varchar(4000),
    use_yn           boolean default true not null,
    creator          varchar(50)          not null,
    create_dtime     timestamp            not null,
    updater          varchar(50)          not null,
    update_dtime     timestamp            not null
);

comment on table scs_cust_add_info is '고객추가정보';

comment on column scs_cust_add_info.cust_add_info_id is '고객추가정보아이디';

comment on constraint "PK_scs_cust_add_info" on scs_cust_add_info is '고객추가정보 기본키';

comment on column scs_cust_add_info.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_add_info.cust_cd is '고객코드';

comment on column scs_cust_add_info.spnote_div_cd is '특이사항구분코드';

comment on column scs_cust_add_info.spnote is '특이사항';

comment on column scs_cust_add_info.use_yn is '사용여부';

comment on column scs_cust_add_info.creator is '생성자';

comment on column scs_cust_add_info.create_dtime is '생성일시';

comment on column scs_cust_add_info.updater is '수정자';

comment on column scs_cust_add_info.update_dtime is '수정일시';

alter table scs_cust_add_info
    owner to ailis_user;

create table scs_cust_contact
(
    cust_contact_id bigserial
        constraint "PK_scs_cust_contact"
            primary key,
    cust_mst_id     varchar(50)          not null,
    cust_cd         varchar(50)          not null,
    acct_charge_nm  varchar(100)         not null,
    ofpo_jbpo       varchar(100),
    telno           varchar(50),
    phno            varchar(50),
    email           varchar(100),
    remark          varchar(4000),
    use_yn          boolean default true not null,
    creator         varchar(50)          not null,
    create_dtime    timestamp            not null,
    updater         varchar(50)          not null,
    update_dtime    timestamp            not null
);

comment on table scs_cust_contact is '고객연락처
';

comment on column scs_cust_contact.cust_contact_id is '고객연락처아이디';

comment on constraint "PK_scs_cust_contact" on scs_cust_contact is '고객연락처 기본키';

comment on column scs_cust_contact.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_contact.cust_cd is '고객코드';

comment on column scs_cust_contact.acct_charge_nm is '거래처담당자명';

comment on column scs_cust_contact.ofpo_jbpo is '직급직책';

comment on column scs_cust_contact.telno is '전화번호';

comment on column scs_cust_contact.phno is '휴대폰번호';

comment on column scs_cust_contact.email is '이메일';

comment on column scs_cust_contact.remark is '비고';

comment on column scs_cust_contact.use_yn is '사용여부';

comment on column scs_cust_contact.creator is '생성자';

comment on column scs_cust_contact.create_dtime is '생성일시';

comment on column scs_cust_contact.updater is '수정자';

comment on column scs_cust_contact.update_dtime is '수정일시';

alter table scs_cust_contact
    owner to ailis_user;

create table scs_sals_action
(
    sals_action_id              bigserial
        constraint "PK_scs_sals_action"
            primary key,
    cust_mst_id                 varchar(50)          not null,
    cust_cd                     varchar(50)          not null,
    visit_dtime                 timestamp            not null,
    visit_prps_cd               varchar(50)          not null,
    visit_target_person_nm      varchar(100),
    visit_target_person_contact varchar(50),
    memo                        varchar(4000),
    use_yn                      boolean default true not null,
    creator                     varchar(50)          not null,
    create_dtime                timestamp            not null,
    updater                     varchar(50)          not null,
    update_dtime                timestamp            not null
);

comment on table scs_sals_action is '영업활동';

comment on column scs_sals_action.sals_action_id is '영업활동아이디';

comment on constraint "PK_scs_sals_action" on scs_sals_action is '영업활동 기본키';

comment on column scs_sals_action.cust_mst_id is '고객마스터아이디';

comment on column scs_sals_action.cust_cd is '고객코드';

comment on column scs_sals_action.visit_dtime is '방문일시';

comment on column scs_sals_action.visit_prps_cd is '방문목적코드';

comment on column scs_sals_action.visit_target_person_nm is '방문대상자(사람)명';

comment on column scs_sals_action.visit_target_person_contact is '방문대상자(사람)연락처';

comment on column scs_sals_action.memo is '메모';

comment on column scs_sals_action.use_yn is '사용여부';

comment on column scs_sals_action.creator is '생성자';

comment on column scs_sals_action.create_dtime is '생성일시';

comment on column scs_sals_action.updater is '수정자';

comment on column scs_sals_action.update_dtime is '수정일시';

alter table scs_sals_action
    owner to ailis_user;

create table scs_req_rst_if_method
(
    req_rst_if_method_id varchar(50)          not null
        constraint "PK_scs_req_rst_if_method"
            primary key,
    cust_mst_id          varchar(50),
    apply_start_dt       date                 not null,
    cust_cd              varchar(50)          not null,
    apply_end_dt         date,
    req_method_cd        varchar(50)          not null,
    req_if_type_cd       varchar(50),
    use_yn               boolean default true not null,
    creator              varchar(50)          not null,
    create_dtime         timestamp            not null,
    updater              varchar(50)          not null,
    update_dtime         timestamp            not null
);

comment on table scs_req_rst_if_method is '의뢰결과연동방법';

comment on column scs_req_rst_if_method.req_rst_if_method_id is '의뢰결과연동메소드(방법)아이디';

comment on constraint "PK_scs_req_rst_if_method" on scs_req_rst_if_method is '의뢰결과연동방법 기본키';

comment on column scs_req_rst_if_method.cust_mst_id is '고객마스터아이디';

comment on column scs_req_rst_if_method.apply_start_dt is '적용시작일자';

comment on column scs_req_rst_if_method.cust_cd is '고객코드';

comment on column scs_req_rst_if_method.apply_end_dt is '적용종료일자';

comment on column scs_req_rst_if_method.req_method_cd is '의뢰메소드(방법)코드';

comment on column scs_req_rst_if_method.req_if_type_cd is '의뢰연동유형';

comment on column scs_req_rst_if_method.use_yn is '사용여부';

comment on column scs_req_rst_if_method.creator is '생성자';

comment on column scs_req_rst_if_method.create_dtime is '생성일시';

comment on column scs_req_rst_if_method.updater is '수정자';

comment on column scs_req_rst_if_method.update_dtime is '수정일시';

alter table scs_req_rst_if_method
    owner to ailis_user;

alter sequence scs_req_rst_if_method_req_rst_if_method_id_seq owned by scs_req_rst_if_method.req_rst_if_method_id;

create table scs_appr_info
(
    appr_info_id       varchar(50) not null
        constraint "PK_scs_appr_info"
            primary key,
    appr_info_no       bigint      not null,
    appr_seq           integer     not null,
    appr_doc_type_cd   varchar(50) not null,
    appr_person_emp_no varchar(50) not null,
    appr_stat_cd       varchar(50) not null,
    appr_cmpl_dtime    timestamp,
    appr_memo          varchar(1000),
    creator            varchar(50) not null,
    create_dtime       timestamp   not null,
    updater            varchar(50) not null,
    update_dtime       timestamp   not null
);

comment on table scs_appr_info is '결재정보';

comment on column scs_appr_info.appr_info_id is '결재정보아이디';

comment on constraint "PK_scs_appr_info" on scs_appr_info is '결재정보 기본키';

comment on column scs_appr_info.appr_info_no is '결재정보번호';

comment on column scs_appr_info.appr_seq is '결재순번';

comment on column scs_appr_info.appr_doc_type_cd is '결재유형코드';

comment on column scs_appr_info.appr_person_emp_no is '결재자(사람)사원번호';

comment on column scs_appr_info.appr_stat_cd is '결재상태코드';

comment on column scs_appr_info.appr_cmpl_dtime is '결재완료일자';

comment on column scs_appr_info.appr_memo is '결재메모';

comment on column scs_appr_info.creator is '생성자';

comment on column scs_appr_info.create_dtime is '생성일시';

comment on column scs_appr_info.updater is '수정자';

comment on column scs_appr_info.update_dtime is '수정일시';

alter table scs_appr_info
    owner to ailis_user;

create unique index scs_appr_info_unique
    on scs_appr_info (appr_info_no, appr_seq);

create table scs_hp_login_user
(
    hp_login_user_id     varchar(50)          not null
        constraint "PK_scs_hp_login_user"
            primary key,
    cust_mst_id          varchar(50)          not null,
    cust_cd              varchar(50)          not null,
    hp_cust_div          varchar(50),
    login_id             varchar(50)          not null,
    login_pswd           varchar(100)         not null,
    login_fail_num       integer,
    pswd_chng_dtime      timestamp,
    last_login_dtime     timestamp,
    login_nm             varchar(100),
    login_person_contact varchar(50),
    use_yn               boolean default true not null,
    creator              varchar(50)          not null,
    create_dtime         timestamp            not null,
    updater              varchar(50)          not null,
    update_dtime         timestamp            not null
);

comment on table scs_hp_login_user is '홈페이지로그인사용자';

comment on column scs_hp_login_user.hp_login_user_id is '홈페이지로그인사용자아이디';

comment on constraint "PK_scs_hp_login_user" on scs_hp_login_user is '홈페이지로그인사용자 기본키';

comment on column scs_hp_login_user.cust_mst_id is '고객마스터아이디';

comment on column scs_hp_login_user.cust_cd is '고객코드';

comment on column scs_hp_login_user.hp_cust_div is '홈페이지고객구분';

comment on column scs_hp_login_user.login_id is '로그인아이디';

comment on column scs_hp_login_user.login_pswd is '로그인패스워드';

comment on column scs_hp_login_user.login_fail_num is '로그인실패횟수';

comment on column scs_hp_login_user.pswd_chng_dtime is '패스워드변경일시';

comment on column scs_hp_login_user.last_login_dtime is '마지막로그인일시';

comment on column scs_hp_login_user.login_nm is '로그인명';

comment on column scs_hp_login_user.login_person_contact is '로그인자(사람)연락처';

comment on column scs_hp_login_user.use_yn is '사용여부';

comment on column scs_hp_login_user.creator is '생성자';

comment on column scs_hp_login_user.create_dtime is '생성일시';

comment on column scs_hp_login_user.updater is '수정자';

comment on column scs_hp_login_user.update_dtime is '수정일시';

alter table scs_hp_login_user
    owner to ailis_user;

create table scs_cust_req_poss_tst_item
(
    cust_req_poss_tst_item_id bigserial
        constraint "PK_scs_cust_req_poss_tst_item"
            primary key,
    cust_mst_id               varchar(50) not null,
    cust_cd                   varchar(50) not null,
    tst_cd                    varchar(10) not null,
    creator                   varchar(50) not null,
    create_dtime              timestamp   not null
);

comment on table scs_cust_req_poss_tst_item is '고객의뢰가능검사항목';

comment on column scs_cust_req_poss_tst_item.cust_req_poss_tst_item_id is '고객의뢰가능검사항목아이디';

comment on constraint "PK_scs_cust_req_poss_tst_item" on scs_cust_req_poss_tst_item is '고객의뢰가능검사항목 기본키';

comment on column scs_cust_req_poss_tst_item.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_req_poss_tst_item.cust_cd is '고객코드';

comment on column scs_cust_req_poss_tst_item.tst_cd is '검사코드';

comment on column scs_cust_req_poss_tst_item.creator is '생성자';

comment on column scs_cust_req_poss_tst_item.create_dtime is '생성일시';

alter table scs_cust_req_poss_tst_item
    owner to ailis_user;

create unique index scs_cust_req_poss_tst_item_unique
    on scs_cust_req_poss_tst_item (cust_cd, tst_cd);

create table scs_cust_tst_cd_mpg
(
    cust_tst_cd_mpg_id varchar(50) not null
        constraint "PK_scs_cust_tst_cd_mpg"
            primary key,
    cust_mst_id        varchar(50),
    cust_cd            varchar(50) not null,
    cust_tst_cd        varchar(50) not null,
    cust_sub_tst_cd    varchar(50),
    cust_tst_nm        varchar(200),
    tst_cd             varchar(10),
    tst_nm             varchar(200),
    creator            varchar(50) not null,
    create_dtime       timestamp   not null,
    updater            varchar(50) not null,
    update_dtime       timestamp   not null
);

comment on table scs_cust_tst_cd_mpg is '고객검사코드맵핑';

comment on column scs_cust_tst_cd_mpg.cust_tst_cd_mpg_id is '고객검사코드맵핑아이디';

comment on constraint "PK_scs_cust_tst_cd_mpg" on scs_cust_tst_cd_mpg is '고객검사코드맵핑 기본키';

comment on column scs_cust_tst_cd_mpg.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_tst_cd_mpg.cust_cd is '고객코드';

comment on column scs_cust_tst_cd_mpg.cust_tst_cd is '고객검사코드';

comment on column scs_cust_tst_cd_mpg.cust_sub_tst_cd is '고객부속검사코드';

comment on column scs_cust_tst_cd_mpg.cust_tst_nm is '고객검사명';

comment on column scs_cust_tst_cd_mpg.tst_cd is '검사코드';

comment on column scs_cust_tst_cd_mpg.tst_nm is '검사명';

comment on column scs_cust_tst_cd_mpg.creator is '생성자';

comment on column scs_cust_tst_cd_mpg.create_dtime is '생성일시';

comment on column scs_cust_tst_cd_mpg.updater is '수정자';

comment on column scs_cust_tst_cd_mpg.update_dtime is '수정일시';

alter table scs_cust_tst_cd_mpg
    owner to ailis_user;

alter sequence scs_cust_tst_cd_mpg_cust_tst_cd_mpg_id_seq owned by scs_cust_tst_cd_mpg.cust_tst_cd_mpg_id;

create table scs_exrt
(
    exrt_id      bigserial
        constraint "PK_scs_exrt"
            primary key,
    stnd_dt      date        not null,
    crcy_cd      varchar(50) not null,
    stnd_exrt    numeric,
    creator      varchar(50) not null,
    create_dtime timestamp   not null,
    updater      varchar(50) not null,
    update_dtime timestamp   not null,
    constraint scs_exrt_unique
        unique (stnd_dt, crcy_cd)
);

comment on table scs_exrt is '환율';

comment on column scs_exrt.exrt_id is '환율아이디';

comment on constraint "PK_scs_exrt" on scs_exrt is '환율 기본키';

comment on column scs_exrt.stnd_dt is '기준일자';

comment on column scs_exrt.crcy_cd is '통화(화폐)코드';

comment on column scs_exrt.stnd_exrt is '기준환율';

comment on column scs_exrt.creator is '생성자';

comment on column scs_exrt.create_dtime is '생성일시';

comment on column scs_exrt.updater is '수정자';

comment on column scs_exrt.update_dtime is '수정일시';

alter table scs_exrt
    owner to ailis_user;

create table scs_cust_cntr
(
    cust_cntr_id  bigserial
        constraint "PK_scs_cust_cntr"
            primary key,
    cust_cd       varchar(50)          not null,
    cust_mst_id   varchar(50)          not null,
    cntr_no       varchar(100)         not null,
    cntr_dt       date                 not null,
    cntr_start_dt date                 not null,
    cntr_end_dt   date,
    cntr_type_cd  varchar(50)          not null,
    recntr_month  varchar(2),
    cntr_nm       varchar(200)         not null,
    cntr_cont     varchar(4000),
    cntr_pic_id   varchar(50),
    atch_grup_id  varchar(50)          not null,
    use_yn        boolean default true not null,
    creator       varchar(50)          not null,
    create_dtime  timestamp            not null,
    updater       varchar(50)          not null,
    update_dtime  timestamp            not null
);

comment on table scs_cust_cntr is '고객계약';

comment on column scs_cust_cntr.cust_cntr_id is '고객계약아이디';

comment on constraint "PK_scs_cust_cntr" on scs_cust_cntr is '고객계약 기본키';

comment on column scs_cust_cntr.cust_cd is '고객코드';

comment on column scs_cust_cntr.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_cntr.cntr_no is '계약번호';

comment on column scs_cust_cntr.cntr_dt is '계약일자';

comment on column scs_cust_cntr.cntr_start_dt is '계약시작일자';

comment on column scs_cust_cntr.cntr_end_dt is '계약종료일자';

comment on column scs_cust_cntr.cntr_type_cd is '계약유형';

comment on column scs_cust_cntr.recntr_month is '재계약월';

comment on column scs_cust_cntr.cntr_nm is '계약제목';

comment on column scs_cust_cntr.cntr_cont is '계약내용';

comment on column scs_cust_cntr.cntr_pic_id is '계약담당자아이디';

comment on column scs_cust_cntr.atch_grup_id is '첨부파일번호';

comment on column scs_cust_cntr.use_yn is '사용여부';

comment on column scs_cust_cntr.creator is '생성자';

comment on column scs_cust_cntr.create_dtime is '생성일시';

comment on column scs_cust_cntr.updater is '수정자';

comment on column scs_cust_cntr.update_dtime is '수정일시';

alter table scs_cust_cntr
    owner to ailis_user;

create table scs_if_cust_info
(
    if_cust_info_id varchar(50) not null
        constraint "PK_scs_if_cust_info"
            primary key,
    cust_mst_id     varchar(50),
    cust_cd         varchar(50) not null,
    header_incl_yn  boolean,
    if_desc         varchar(500),
    skip_row_cnt    integer,
    creator         varchar(50) not null,
    create_dtime    timestamp   not null,
    updater         varchar(50) not null,
    update_dtime    timestamp   not null
);

comment on table scs_if_cust_info is '연동고객정보';

comment on column scs_if_cust_info.if_cust_info_id is '연동고객아이디';

comment on constraint "PK_scs_if_cust_info" on scs_if_cust_info is '연동고객정보 기본키';

comment on column scs_if_cust_info.cust_mst_id is '고객마스터아이디';

comment on column scs_if_cust_info.cust_cd is '고객코드';

comment on column scs_if_cust_info.header_incl_yn is '헤더포함여부';

comment on column scs_if_cust_info.if_desc is '연동설명';

comment on column scs_if_cust_info.skip_row_cnt is '스킵행(로우)수(건수)';

comment on column scs_if_cust_info.creator is '생성자';

comment on column scs_if_cust_info.create_dtime is '생성일시';

comment on column scs_if_cust_info.updater is '수정자';

comment on column scs_if_cust_info.update_dtime is '수정일시';

alter table scs_if_cust_info
    owner to ailis_user;

create unique index "UK_scs_if_cust_info_cust_mst_id"
    on scs_if_cust_info (cust_mst_id);

create table scs_if_field_info
(
    if_field_info_id varchar(50) not null
        constraint "PK_scs_if_field_info"
            primary key,
    if_field_nm      varchar(100),
    if_field_col_nm  varchar(100),
    if_field_exps    varchar(500),
    if_field_desc    varchar(500),
    creator          varchar(50) not null,
    create_dtime     timestamp   not null,
    updater          varchar(50) not null,
    update_dtime     timestamp   not null
);

comment on table scs_if_field_info is '연동필드정보';

comment on column scs_if_field_info.if_field_info_id is '연동필드아이디';

comment on constraint "PK_scs_if_field_info" on scs_if_field_info is '연동필드정보 기본키';

comment on column scs_if_field_info.if_field_nm is '연동필드명';

comment on column scs_if_field_info.if_field_col_nm is '연동필드열(컬럼)명';

comment on column scs_if_field_info.if_field_exps is '연동필드표현식';

comment on column scs_if_field_info.if_field_desc is '연동필드설명';

comment on column scs_if_field_info.creator is '생성자';

comment on column scs_if_field_info.create_dtime is '생성일시';

comment on column scs_if_field_info.updater is '수정자';

comment on column scs_if_field_info.update_dtime is '수정일시';

alter table scs_if_field_info
    owner to ailis_user;

create table scs_if_conf_info
(
    if_conf_info_id  varchar(50) not null
        constraint "PK_scs_if_conf_info"
            primary key,
    if_cust_info_id  varchar(50),
    if_field_info_id varchar(50),
    col_idx          integer,
    creator          varchar(50) not null,
    create_dtime     timestamp   not null,
    updater          varchar(50) not null,
    update_dtime     timestamp   not null
);

comment on table scs_if_conf_info is '연동설정정보';

comment on column scs_if_conf_info.if_conf_info_id is '연동설정정보아이디';

comment on constraint "PK_scs_if_conf_info" on scs_if_conf_info is '연동설정정보 기본키';

comment on column scs_if_conf_info.if_cust_info_id is '연동고객정보아이디';

comment on column scs_if_conf_info.if_field_info_id is '연동필드정보아이디';

comment on column scs_if_conf_info.col_idx is '열(컬럼)인덱스';

comment on column scs_if_conf_info.creator is '생성자';

comment on column scs_if_conf_info.create_dtime is '생성일시';

comment on column scs_if_conf_info.updater is '수정자';

comment on column scs_if_conf_info.update_dtime is '수정일시';

alter table scs_if_conf_info
    owner to ailis_user;

create unique index scs_if_conf_info_unique1
    on scs_if_conf_info (if_cust_info_id, col_idx);

create unique index scs_if_conf_info_unique2
    on scs_if_conf_info (if_cust_info_id, if_field_info_id);

create table scs_cust_charge
(
    cust_charge_id    varchar(50) not null
        constraint "PK_scs_cust_charge"
            primary key,
    cust_mst_id       varchar(50),
    cust_cd           varchar(50) not null,
    apply_start_dt    date        not null,
    apply_end_dt      date        not null,
    tst_cd            varchar(10) not null,
    crcy_cd           varchar(50) not null,
    stnd_price        numeric,
    special_charge    numeric     not null,
    supval            numeric,
    addtax            numeric,
    remark            varchar(4000),
    appr_info_no      bigint,
    curr_appr_seq     integer,
    appr_subms_emp_no varchar(50),
    appr_subms_dtime  timestamp,
    last_appr_stat_cd varchar(50) not null,
    appr_lvl_cd       varchar(50),
    creator           varchar(50) not null,
    create_dtime      timestamp   not null,
    updater           varchar(50) not null,
    update_dtime      timestamp   not null
);

comment on table scs_cust_charge is '고객수가';

comment on column scs_cust_charge.cust_charge_id is '고객수가아이디';

comment on constraint "PK_scs_cust_charge" on scs_cust_charge is '고객수가 기본키';

comment on column scs_cust_charge.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_charge.cust_cd is '고객코드';

comment on column scs_cust_charge.apply_start_dt is '적용시작일자';

comment on column scs_cust_charge.apply_end_dt is '적용종료일자';

comment on column scs_cust_charge.tst_cd is '검사코드';

comment on column scs_cust_charge.crcy_cd is '통화(화폐)코드';

comment on column scs_cust_charge.stnd_price is '기준가(가격)';

comment on column scs_cust_charge.special_charge is '특별수가';

comment on column scs_cust_charge.supval is '공급가액';

comment on column scs_cust_charge.addtax is '부가세';

comment on column scs_cust_charge.remark is '비고';

comment on column scs_cust_charge.appr_info_no is '결재정보번호';

comment on column scs_cust_charge.curr_appr_seq is '현재결재순번';

comment on column scs_cust_charge.appr_subms_emp_no is '결재상신사원번호';

comment on column scs_cust_charge.appr_subms_dtime is '결재상신일시';

comment on column scs_cust_charge.last_appr_stat_cd is '마지막결재상태코드';

comment on column scs_cust_charge.appr_lvl_cd is '결재레벨코드';

comment on column scs_cust_charge.creator is '생성자';

comment on column scs_cust_charge.create_dtime is '생성일시';

comment on column scs_cust_charge.updater is '수정자';

comment on column scs_cust_charge.update_dtime is '수정일시';

alter table scs_cust_charge
    owner to ailis_user;

alter sequence scs_cust_charge_cust_charge_id_seq owned by scs_cust_charge.cust_charge_id;

create unique index scs_cust_charge_unique
    on scs_cust_charge (cust_cd, apply_start_dt, tst_cd);

create table scs_cust_mst_tmp
(
    cust_mst_id                 varchar(50),
    cust_cd                     varchar(50),
    cust_nm                     varchar(100),
    rst_output_cust_nm          varchar(100),
    rprs_nm                     varchar(50),
    rprs_cust_yn                boolean,
    rprs_cust_cd                varchar(50),
    cust_div_cd                 varchar(50),
    direct_acct_cd              varchar(50),
    direct_acct_acct_cd         varchar(50),
    frgn_acct_yn                boolean,
    study_proj_cust_yn          boolean,
    study_proj_nm               varchar(200),
    natn_cd                     varchar(50),
    use_lang_cd                 varchar(50),
    crcy_cd                     varchar(50),
    cust_stat_cd                varchar(50),
    req_poss_yn                 boolean,
    cust_type_cd                varchar(50),
    cust_grade_cd               varchar(50),
    branch_cd                   varchar(50),
    bzoffi_cd                   varchar(50),
    bzoffi_pic_id               varchar(50),
    asrt_cd                     varchar(50),
    zipcd                       varchar(10),
    addr01                      varchar(500),
    addr2                       varchar(500),
    hp_url                      varchar(200),
    care_inst_no                varchar(50),
    care_inst_id                varchar(50),
    bizrno                      varchar(50),
    corp_no                     varchar(50),
    bzse                        varchar(50),
    bztp                        varchar(50),
    biznm                       varchar(100),
    bizreg_rprs_nm              varchar(100),
    open_dt                     date,
    bill_publ_yn                boolean,
    bill_auto_publ_target_yn    boolean,
    addtax_incl_yn              boolean,
    tax_div_cd                  varchar(50),
    bill_pic                    varchar(100),
    bill_pic_emai_addr          varchar(100),
    bill_pic_telno              varchar(50),
    bill_publ_dt                integer,
    rprs_acct_bill_comb_publ_yn boolean,
    pay_rtday                   integer,
    pay_plan_dt                 integer,
    pay_method_cd               varchar(50),
    gcc_stmt_method_cd          varchar(50),
    sap_cust_cd                 varchar(50),
    spcm_pickup_method_cd       varchar(50),
    gcg_pickup_pic_emp_no       varchar(50),
    trunc_unit_cd               varchar(50),
    invc_email_recp_yn          boolean,
    invc_recp_email_addr        varchar(100),
    outamt_writing_yn           boolean,
    sot_output_yn               boolean,
    sot_output_qnty             integer,
    rst_ntcn_recp_yn            boolean,
    rst_ntcn_recp_email_addr    varchar(100),
    creator                     varchar(50),
    create_dtime                timestamp,
    updater                     varchar(50),
    update_dtime                timestamp
);

alter table scs_cust_mst_tmp
    owner to ailis_user;

create table scs_cust_mst
(
    cust_mst_id                 varchar(50)           not null
        constraint "PK_scs_cust_mst"
            primary key,
    cust_cd                     varchar(50)           not null,
    cust_nm                     varchar(100)          not null,
    rst_output_cust_nm          varchar(100),
    rprs_nm                     varchar(50),
    rprs_cust_yn                boolean               not null,
    rprs_cust_cd                varchar(50),
    cust_div_cd                 varchar(50)           not null,
    direct_acct_cd              varchar(50),
    direct_acct_acct_cd         varchar(50),
    frgn_acct_yn                boolean               not null,
    study_proj_cust_yn          boolean               not null,
    study_proj_nm               varchar(200),
    natn_cd                     varchar(50)           not null,
    use_lang_cd                 varchar(50)           not null,
    crcy_cd                     varchar(50)           not null,
    cust_stat_cd                varchar(50)           not null,
    req_poss_yn                 boolean               not null,
    cust_type_cd                varchar(50)           not null,
    cust_grade_cd               varchar(50),
    branch_cd                   varchar(50),
    bzoffi_cd                   varchar(50),
    bzoffi_pic_id               varchar(50),
    asrt_cd                     varchar(50),
    zipcd                       varchar(10),
    addr1                       varchar(500),
    addr2                       varchar(500),
    hp_url                      varchar(200),
    care_inst_no                varchar(50),
    care_inst_id                varchar(50),
    bizrno                      varchar(50),
    corp_no                     varchar(50),
    bzse                        varchar(50),
    bztp                        varchar(50),
    biznm                       varchar(100),
    bizreg_rprs_nm              varchar(100),
    open_dt                     date,
    bill_publ_yn                boolean               not null,
    bill_auto_publ_target_yn    boolean               not null,
    addtax_incl_yn              boolean               not null,
    tax_div_cd                  varchar(50)           not null,
    bill_pic                    varchar(100),
    bill_pic_emai_addr          varchar(100),
    bill_pic_telno              varchar(50),
    bill_publ_dt                integer,
    rprs_acct_bill_comb_publ_yn boolean               not null,
    pay_rtday                   integer,
    pay_plan_dt                 integer,
    pay_method_cd               varchar(50),
    gcc_stmt_method_cd          varchar(50)           not null,
    sap_cust_cd                 varchar(50),
    spcm_pickup_method_cd       varchar(50),
    gcg_pickup_pic_emp_no       varchar(50),
    trunc_unit_cd               varchar(50),
    invc_email_recp_yn          boolean               not null,
    invc_recp_email_addr        varchar(100),
    outamt_writing_yn           boolean               not null,
    sot_output_yn               boolean               not null,
    sot_output_qnty             integer               not null,
    rst_ntcn_recp_yn            boolean               not null,
    rst_ntcn_recp_email_addr    varchar(100),
    req_method_cd               varchar(50),
    req_if_type_cd              varchar(50),
    creator                     varchar(50)           not null,
    create_dtime                timestamp             not null,
    updater                     varchar(50)           not null,
    update_dtime                timestamp             not null,
    req_div_cd                  varchar(50)           not null,
    tel_no                      varchar(50),
    fax_no                      varchar(50),
    atch_file_grup_id           varchar(50),
    req_poss_tst_limit_yn       boolean default false not null
);

comment on table scs_cust_mst is '고객마스터';

comment on column scs_cust_mst.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_mst.cust_cd is '고객코드';

comment on column scs_cust_mst.cust_nm is '고객명';

comment on column scs_cust_mst.rst_output_cust_nm is '결과출력고객명';

comment on column scs_cust_mst.rprs_nm is '대표명';

comment on column scs_cust_mst.rprs_cust_yn is '대표고객여부';

comment on column scs_cust_mst.rprs_cust_cd is '대표고객코드';

comment on column scs_cust_mst.cust_div_cd is '고객구분코드';

comment on column scs_cust_mst.direct_acct_cd is '직접거래처코드';

comment on column scs_cust_mst.direct_acct_acct_cd is '직접거래처거래처코드';

comment on column scs_cust_mst.frgn_acct_yn is '해외거래처여부';

comment on column scs_cust_mst.study_proj_cust_yn is '연구과제고객여부';

comment on column scs_cust_mst.study_proj_nm is '연구과제명';

comment on column scs_cust_mst.natn_cd is '국가코드';

comment on column scs_cust_mst.use_lang_cd is '사용언어코드';

comment on column scs_cust_mst.crcy_cd is '통화(화폐)코드';

comment on column scs_cust_mst.cust_stat_cd is '고객상태코드';

comment on column scs_cust_mst.req_poss_yn is '의뢰가능여부';

comment on column scs_cust_mst.cust_type_cd is '고객유형코드';

comment on column scs_cust_mst.cust_grade_cd is '고객등급코드';

comment on column scs_cust_mst.branch_cd is '지점코드';

comment on column scs_cust_mst.bzoffi_cd is '영업소코드';

comment on column scs_cust_mst.bzoffi_pic_id is '영업소담당자아이디';

comment on column scs_cust_mst.asrt_cd is '종별코드';

comment on column scs_cust_mst.zipcd is '우편번호';

comment on column scs_cust_mst.addr1 is '주소1';

comment on column scs_cust_mst.addr2 is '주소2';

comment on column scs_cust_mst.hp_url is '홈페이지URL';

comment on column scs_cust_mst.care_inst_no is '요양기관번호';

comment on column scs_cust_mst.care_inst_id is '요양기관아이디';

comment on column scs_cust_mst.bizrno is '사업자등록번호';

comment on column scs_cust_mst.corp_no is '법인번호';

comment on column scs_cust_mst.bzse is '업종';

comment on column scs_cust_mst.bztp is '업태';

comment on column scs_cust_mst.biznm is '사업자명';

comment on column scs_cust_mst.bizreg_rprs_nm is '사업자등록대표(자)명';

comment on column scs_cust_mst.open_dt is '개설(개업)일자';

comment on column scs_cust_mst.bill_publ_yn is '계산서발행여부';

comment on column scs_cust_mst.bill_auto_publ_target_yn is '계산서자동발행대상여부';

comment on column scs_cust_mst.addtax_incl_yn is '부가세포함여부';

comment on column scs_cust_mst.tax_div_cd is '과세구분코드';

comment on column scs_cust_mst.bill_pic is '계산서담당자';

comment on column scs_cust_mst.bill_pic_emai_addr is '계산서담당자주소';

comment on column scs_cust_mst.bill_pic_telno is '계산서담당자전화번호';

comment on column scs_cust_mst.bill_publ_dt is '계산서발행일자';

comment on column scs_cust_mst.rprs_acct_bill_comb_publ_yn is '대표(자)거래처계산서통합(일반)발행여부';

comment on column scs_cust_mst.pay_rtday is '결제회전일수';

comment on column scs_cust_mst.pay_plan_dt is '결제예정(계획)일자';

comment on column scs_cust_mst.pay_method_cd is '결제메소드(방법)코드';

comment on column scs_cust_mst.gcc_stmt_method_cd is 'GCCell정산메소드(방법)코드';

comment on column scs_cust_mst.sap_cust_cd is 'SAP고객코드';

comment on column scs_cust_mst.spcm_pickup_method_cd is '검체수거수가코드';

comment on column scs_cust_mst.gcg_pickup_pic_emp_no is 'GC Genome수거담당자사원번호';

comment on column scs_cust_mst.trunc_unit_cd is '절사단위코드';

comment on column scs_cust_mst.invc_email_recp_yn is '청구서이메일수신여부';

comment on column scs_cust_mst.invc_recp_email_addr is '청구서수신이메일계정';

comment on column scs_cust_mst.outamt_writing_yn is '미수금표기여부';

comment on column scs_cust_mst.sot_output_yn is '거래명세서출력여부';

comment on column scs_cust_mst.sot_output_qnty is '거래명세서출력수량';

comment on column scs_cust_mst.rst_ntcn_recp_yn is '결과알림수신여부';

comment on column scs_cust_mst.rst_ntcn_recp_email_addr is '결과알림수신메일';

comment on column scs_cust_mst.req_method_cd is '의뢰메소드(방법)코드';

comment on column scs_cust_mst.req_if_type_cd is '의뢰연동유형코드';

comment on column scs_cust_mst.creator is '생성자';

comment on column scs_cust_mst.create_dtime is '생성일시';

comment on column scs_cust_mst.updater is '수정자';

comment on column scs_cust_mst.update_dtime is '수정일시';

comment on column scs_cust_mst.req_div_cd is '의뢰구분코드';

comment on column scs_cust_mst.tel_no is '전화번호';

comment on column scs_cust_mst.fax_no is '팩스번호';

comment on column scs_cust_mst.atch_file_grup_id is '첨부파일그룹아이디';

comment on column scs_cust_mst.req_poss_tst_limit_yn is '의뢰가능검사제한여부';

alter table scs_cust_mst
    owner to ailis_user;

create unique index scs_cust_mst_unique
    on scs_cust_mst (cust_cd);

create table scs_gcgn_sals_pic_info
(
    gcgn_sals_pic_info_id bigserial
        constraint "PK_scs_gcgn_sals_pic_info"
            primary key,
    cust_mst_id           varchar(50),
    cust_cd               varchar(50) not null,
    apply_start_dt        date        not null,
    apply_end_dt          date,
    sals_team_cd          varchar(50) not null,
    emp_user_id           varchar(50) not null,
    creator               varchar(50) not null,
    create_dtime          timestamp   not null,
    updater               varchar(50) not null,
    update_dtime          timestamp   not null
);

comment on table scs_gcgn_sals_pic_info is '지놈영업담당자정보';

comment on column scs_gcgn_sals_pic_info.gcgn_sals_pic_info_id is '지놈영업담당자정보아이디';

comment on constraint "PK_scs_gcgn_sals_pic_info" on scs_gcgn_sals_pic_info is '지놈영업담당자정보 기본키';

comment on column scs_gcgn_sals_pic_info.cust_mst_id is '고객마스터아이디';

comment on column scs_gcgn_sals_pic_info.cust_cd is '고객코드';

comment on column scs_gcgn_sals_pic_info.apply_start_dt is '적용시작일자';

comment on column scs_gcgn_sals_pic_info.apply_end_dt is '적용종료일자';

comment on column scs_gcgn_sals_pic_info.sals_team_cd is '영업팀코드';

comment on column scs_gcgn_sals_pic_info.emp_user_id is '사원사용자아이디';

comment on column scs_gcgn_sals_pic_info.creator is '생성자';

comment on column scs_gcgn_sals_pic_info.create_dtime is '생성일시';

comment on column scs_gcgn_sals_pic_info.updater is '수정자';

comment on column scs_gcgn_sals_pic_info.update_dtime is '수정일시';

alter table scs_gcgn_sals_pic_info
    owner to ailis_user;

create table scs_cust_mst_hst
(
    cust_mst_hst_id             bigserial
        constraint "PK_scs_cust_mst_hst"
            primary key,
    cust_mst_id                 varchar(50)  not null,
    cust_cd                     varchar(50)  not null,
    cust_nm                     varchar(100) not null,
    rst_output_cust_nm          varchar(100),
    rprs_nm                     varchar(50),
    rprs_cust_yn                boolean      not null,
    rprs_cust_cd                varchar(50),
    cust_div_cd                 varchar(50)  not null,
    direct_acct_cd              varchar(50),
    direct_acct_acct_cd         varchar(50),
    frgn_acct_yn                boolean      not null,
    study_proj_cust_yn          boolean      not null,
    study_proj_nm               varchar(200),
    natn_cd                     varchar(50)  not null,
    use_lang_cd                 varchar(50)  not null,
    crcy_cd                     varchar(50)  not null,
    cust_stat_cd                varchar(50)  not null,
    req_poss_yn                 boolean      not null,
    cust_type_cd                varchar(50)  not null,
    cust_grade_cd               varchar(50),
    branch_cd                   varchar(50),
    bzoffi_cd                   varchar(50),
    bzoffi_pic_id               varchar(50),
    asrt_cd                     varchar(50),
    zipcd                       varchar(10),
    addr1                       varchar(500),
    addr2                       varchar(500),
    hp_url                      varchar(200),
    care_inst_no                varchar(50),
    care_inst_id                varchar(50),
    bizrno                      varchar(50),
    corp_no                     varchar(50),
    bzse                        varchar(50),
    bztp                        varchar(50),
    biznm                       varchar(100),
    bizreg_rprs_nm              varchar(100),
    open_dt                     date,
    bill_publ_yn                boolean      not null,
    bill_auto_publ_target_yn    boolean      not null,
    addtax_incl_yn              boolean      not null,
    tax_div_cd                  varchar(50)  not null,
    bill_pic                    varchar(100),
    bill_pic_emai_addr          varchar(100),
    bill_pic_telno              varchar(50),
    bill_publ_dt                integer,
    rprs_acct_bill_comb_publ_yn boolean      not null,
    pay_rtday                   integer,
    pay_plan_dt                 integer,
    pay_method_cd               varchar(50),
    gcc_stmt_method_cd          varchar(50)  not null,
    sap_cust_cd                 varchar(50),
    spcm_pickup_method_cd       varchar(50),
    gcg_pickup_pic_emp_no       varchar(50),
    trunc_unit_cd               varchar(50),
    invc_email_recp_yn          boolean      not null,
    invc_recp_email_addr        varchar(100),
    outamt_writing_yn           boolean      not null,
    sot_output_yn               boolean      not null,
    sot_output_qnty             integer      not null,
    rst_ntcn_recp_yn            boolean      not null,
    rst_ntcn_recp_email_addr    varchar(100),
    req_method_cd               varchar(50),
    req_if_type_cd              varchar(50),
    creator                     varchar(50)  not null,
    create_dtime                timestamp    not null,
    updater                     varchar(50)  not null,
    update_dtime                timestamp    not null,
    req_div_cd                  varchar(50)  not null,
    tel_no                      varchar(50),
    fax_no                      varchar(50),
    atch_file_grup_id           varchar(50),
    req_poss_tst_limit_yn       boolean,
    update_reason               varchar(1000)
);

comment on table scs_cust_mst_hst is '고객마스터이력';

comment on column scs_cust_mst_hst.cust_mst_hst_id is '고객마스터이력아이디';

comment on column scs_cust_mst_hst.cust_mst_id is '고객마스터아이디';

comment on column scs_cust_mst_hst.cust_cd is '고객코드';

comment on column scs_cust_mst_hst.cust_nm is '고객명';

comment on column scs_cust_mst_hst.rst_output_cust_nm is '결과출력고객명';

comment on column scs_cust_mst_hst.rprs_nm is '대표명';

comment on column scs_cust_mst_hst.rprs_cust_yn is '대표고객여부';

comment on column scs_cust_mst_hst.rprs_cust_cd is '대표고객코드';

comment on column scs_cust_mst_hst.cust_div_cd is '고객구분코드';

comment on column scs_cust_mst_hst.direct_acct_cd is '직접거래처코드';

comment on column scs_cust_mst_hst.direct_acct_acct_cd is '직접거래처거래처코드';

comment on column scs_cust_mst_hst.frgn_acct_yn is '해외거래처여부';

comment on column scs_cust_mst_hst.study_proj_cust_yn is '연구과제고객여부';

comment on column scs_cust_mst_hst.study_proj_nm is '연구과제명';

comment on column scs_cust_mst_hst.natn_cd is '국가코드';

comment on column scs_cust_mst_hst.use_lang_cd is '사용언어코드';

comment on column scs_cust_mst_hst.crcy_cd is '통화(화폐)코드';

comment on column scs_cust_mst_hst.cust_stat_cd is '고객상태코드';

comment on column scs_cust_mst_hst.req_poss_yn is '의뢰가능여부';

comment on column scs_cust_mst_hst.cust_type_cd is '고객유형코드';

comment on column scs_cust_mst_hst.cust_grade_cd is '고객등급코드';

comment on column scs_cust_mst_hst.branch_cd is '지점코드';

comment on column scs_cust_mst_hst.bzoffi_cd is '영업소코드';

comment on column scs_cust_mst_hst.bzoffi_pic_id is '영업소담당자아이디';

comment on column scs_cust_mst_hst.asrt_cd is '종별코드';

comment on column scs_cust_mst_hst.zipcd is '우편번호';

comment on column scs_cust_mst_hst.addr1 is '주소1';

comment on column scs_cust_mst_hst.addr2 is '주소2';

comment on column scs_cust_mst_hst.hp_url is '홈페이지URL';

comment on column scs_cust_mst_hst.care_inst_no is '요양기관번호';

comment on column scs_cust_mst_hst.care_inst_id is '요양기관아이디';

comment on column scs_cust_mst_hst.bizrno is '사업자등록번호';

comment on column scs_cust_mst_hst.corp_no is '법인번호';

comment on column scs_cust_mst_hst.bzse is '업종';

comment on column scs_cust_mst_hst.bztp is '업태';

comment on column scs_cust_mst_hst.biznm is '사업자명';

comment on column scs_cust_mst_hst.bizreg_rprs_nm is '사업자등록대표(자)명';

comment on column scs_cust_mst_hst.open_dt is '개설(개업)일자';

comment on column scs_cust_mst_hst.bill_publ_yn is '계산서발행여부';

comment on column scs_cust_mst_hst.bill_auto_publ_target_yn is '계산서자동발행대상여부';

comment on column scs_cust_mst_hst.addtax_incl_yn is '부가세포함여부';

comment on column scs_cust_mst_hst.tax_div_cd is '과세구분코드';

comment on column scs_cust_mst_hst.bill_pic is '계산서담당자';

comment on column scs_cust_mst_hst.bill_pic_emai_addr is '계산서담당자주소';

comment on column scs_cust_mst_hst.bill_pic_telno is '계산서담당자전화번호';

comment on column scs_cust_mst_hst.bill_publ_dt is '계산서발행일자';

comment on column scs_cust_mst_hst.rprs_acct_bill_comb_publ_yn is '대표(자)거래처계산서통합(일반)발행여부';

comment on column scs_cust_mst_hst.pay_rtday is '결제회전일수';

comment on column scs_cust_mst_hst.pay_plan_dt is '결제예정(계획)일자';

comment on column scs_cust_mst_hst.pay_method_cd is '결제메소드(방법)코드';

comment on column scs_cust_mst_hst.gcc_stmt_method_cd is 'GCCell정산메소드(방법)코드';

comment on column scs_cust_mst_hst.sap_cust_cd is 'SAP고객코드';

comment on column scs_cust_mst_hst.spcm_pickup_method_cd is '검체수거수가코드';

comment on column scs_cust_mst_hst.gcg_pickup_pic_emp_no is 'GC Genome수거담당자사원번호';

comment on column scs_cust_mst_hst.trunc_unit_cd is '절사단위코드';

comment on column scs_cust_mst_hst.invc_email_recp_yn is '청구서이메일수신여부';

comment on column scs_cust_mst_hst.invc_recp_email_addr is '청구서수신이메일계정';

comment on column scs_cust_mst_hst.outamt_writing_yn is '미수금표기여부';

comment on column scs_cust_mst_hst.sot_output_yn is '거래명세서출력여부';

comment on column scs_cust_mst_hst.sot_output_qnty is '거래명세서출력수량';

comment on column scs_cust_mst_hst.rst_ntcn_recp_yn is '결과알림수신여부';

comment on column scs_cust_mst_hst.rst_ntcn_recp_email_addr is '결과알림수신메일';

comment on column scs_cust_mst_hst.req_method_cd is '의뢰메소드(방법)코드';

comment on column scs_cust_mst_hst.req_if_type_cd is '의뢰연동유형코드';

comment on column scs_cust_mst_hst.creator is '생성자';

comment on column scs_cust_mst_hst.create_dtime is '생성일시';

comment on column scs_cust_mst_hst.updater is '수정자';

comment on column scs_cust_mst_hst.update_dtime is '수정일시';

comment on column scs_cust_mst_hst.req_div_cd is '의뢰구분코드';

comment on column scs_cust_mst_hst.tel_no is '전화번호';

comment on column scs_cust_mst_hst.fax_no is '팩스번호';

comment on column scs_cust_mst_hst.atch_file_grup_id is '첨부파일그룹번호';

comment on column scs_cust_mst_hst.req_poss_tst_limit_yn is '의뢰가능검사제한여부';

comment on column scs_cust_mst_hst.update_reason is '수정사유';

alter table scs_cust_mst_hst
    owner to ailis_user;

create table scs_cust_atch_file
(
    cust_atch_file_id      varchar(50)          not null
        constraint "PK_scs_cust_atch_file"
            primary key,
    cust_atch_file_grup_id varchar(50)          not null,
    doc_type_cd            varchar(50)          not null,
    atch_grup_id           varchar(50)          not null,
    use_yn                 boolean default true not null,
    creator                varchar(50)          not null,
    create_dtime           timestamp            not null,
    updater                varchar(50)          not null,
    update_dtime           timestamp            not null
);

comment on table scs_cust_atch_file is '고객첨부파일';

comment on column scs_cust_atch_file.cust_atch_file_id is '고객첨부파일아이디';

comment on constraint "PK_scs_cust_atch_file" on scs_cust_atch_file is '고객첨부파일 기본키';

comment on column scs_cust_atch_file.cust_atch_file_grup_id is '고객첨부파일그룹아이디';

comment on column scs_cust_atch_file.doc_type_cd is '문서유형코드';

comment on column scs_cust_atch_file.atch_grup_id is '첨부파일번호';

comment on column scs_cust_atch_file.use_yn is '사용여부';

comment on column scs_cust_atch_file.creator is '생성자';

comment on column scs_cust_atch_file.create_dtime is '생성일시';

comment on column scs_cust_atch_file.updater is '수정자';

comment on column scs_cust_atch_file.update_dtime is '수정일시';

alter table scs_cust_atch_file
    owner to ailis_user;

create table sbl_demand
(
    demand_id             varchar(50)           not null
        constraint "PK_sbl_demand"
            primary key,
    demand_dt             date                  not null,
    cust_cd               varchar(50)           not null,
    demand_start_dt       date                  not null,
    demand_end_dt         date                  not null,
    stnd_price            numeric,
    supval                numeric,
    demand_charge         numeric,
    addtax                numeric,
    dscnt_rate            numeric,
    demand_create_dtime   timestamp,
    demand_creator_emp_no varchar(50),
    insu_price            numeric,
    invc_output_dtime     timestamp,
    invc_output_empno     varchar(50),
    slstmt_no             varchar(50),
    slstmt_send_dt        date,
    slstmt_send_emp_no    varchar(50),
    demand_memo           varchar(4000),
    sap_cust_cd           varchar(50),
    bill_publ_yn          boolean default false not null,
    invc_recp_email_addr  varchar(100),
    creator               varchar(50)           not null,
    create_dtime          timestamp             not null,
    updater               varchar(50)           not null,
    update_dtime          timestamp             not null,
    colledger_id          varchar(50)
);

comment on table sbl_demand is '청구';

comment on column sbl_demand.demand_id is '청구아이디 UUID';

comment on constraint "PK_sbl_demand" on sbl_demand is '청구 기본키';

comment on column sbl_demand.demand_dt is '청구일자';

comment on column sbl_demand.cust_cd is '고객코드';

comment on column sbl_demand.demand_start_dt is '청구시작일자';

comment on column sbl_demand.demand_end_dt is '청구종료일자';

comment on column sbl_demand.stnd_price is '기준가';

comment on column sbl_demand.supval is '공급가액';

comment on column sbl_demand.demand_charge is '청구수가';

comment on column sbl_demand.addtax is '부가세';

comment on column sbl_demand.dscnt_rate is '할인율';

comment on column sbl_demand.demand_create_dtime is '청구생성일시';

comment on column sbl_demand.demand_creator_emp_no is '청구생성자사번';

comment on column sbl_demand.insu_price is '보험가';

comment on column sbl_demand.invc_output_dtime is '청구서출력일시';

comment on column sbl_demand.invc_output_empno is '청구서출력사원번호';

comment on column sbl_demand.slstmt_no is '전표번호';

comment on column sbl_demand.slstmt_send_dt is '전표전송일자';

comment on column sbl_demand.slstmt_send_emp_no is '전표전송사원번호';

comment on column sbl_demand.demand_memo is '청구메모';

comment on column sbl_demand.sap_cust_cd is 'SAP고객코드';

comment on column sbl_demand.bill_publ_yn is '계산서발행여부';

comment on column sbl_demand.invc_recp_email_addr is '청구서수신이메일계정';

comment on column sbl_demand.creator is '생성자';

comment on column sbl_demand.create_dtime is '생성일시';

comment on column sbl_demand.updater is '수정자';

comment on column sbl_demand.update_dtime is '수정일시';

comment on column sbl_demand.colledger_id is '청구수금원장아이디';

alter table sbl_demand
    owner to ailis_user;

create table sbl_demand_hst
(
    demand_hst_id         varchar(50)           not null
        constraint "PK_sbl_demand_hst"
            primary key,
    hst_cd                varchar(50)           not null,
    hst_memo              varchar(500)          not null,
    worker                varchar(50)           not null,
    work_dtime            timestamp             not null,
    demand_id             varchar(50)           not null,
    demand_dt             date                  not null,
    cust_cd               varchar(50)           not null,
    demand_start_dt       date                  not null,
    demand_end_dt         date                  not null,
    stnd_price            numeric,
    supval                numeric,
    demand_charge         numeric,
    addtax                numeric,
    dscnt_rate            numeric,
    demand_create_dtime   timestamp,
    demand_creator_emp_no varchar(50),
    insure_price          numeric,
    invc_output_dtime     timestamp,
    invc_output_empno     varchar(50),
    slstmt_no             varchar(50),
    slstmt_send_dt        date,
    slstmt_send_emp_no    varchar(50),
    demand_memo           varchar(4000),
    sap_cust_cd           varchar(50),
    bill_publ_yn          boolean default false not null,
    invc_recp_email_addr  varchar(100),
    creator               varchar(50)           not null,
    create_dtime          timestamp             not null,
    updater               varchar(50)           not null,
    update_dtime          timestamp             not null,
    colledger_id          varchar(50)
);

comment on table sbl_demand_hst is '청구 이력';
comment on column sbl_demand_hst.demand_hst_id is '청구이력아이디 UUID';
comment on column sbl_demand_hst.hst_cd is '이력코드';
comment on column sbl_demand_hst.hst_memo is '이력메모';
comment on column sbl_demand_hst.worker is '작업자';
comment on column sbl_demand_hst.work_dtime is '작업일시';
comment on column sbl_demand_hst.demand_id is '청구아이디';

alter table sbl_demand_hst
    owner to ailis_user;

create table sbl_colledger
(
    colledger_id     varchar(50)       not null
        constraint "PK_sbl_colledger"
            primary key,
    colbill_div_cd   varchar(50)       not null,
    colbill_dt       date              not null,
    cust_cd          varchar(50)       not null,
    colbill_item_nm  varchar(50)       not null,
    colbill_item_dtl varchar(200),
    colbill_amt      numeric default 0 not null,
    colbill_memo     varchar(4000),
    creator          varchar(50)       not null,
    create_dtime     timestamp         not null,
    updater          varchar(50)       not null,
    update_dtime     timestamp         not null
);

comment on table sbl_colledger is '청구수금원장';

comment on column sbl_colledger.colledger_id is '수금원장아이디';

comment on constraint "PK_sbl_colledger" on sbl_colledger is '청구수금원장 기본키';

comment on column sbl_colledger.colbill_div_cd is '수금구분코드';

comment on column sbl_colledger.colbill_dt is '수금일자';

comment on column sbl_colledger.cust_cd is '고객코드';

comment on column sbl_colledger.colbill_item_nm is '수금항목명';

comment on column sbl_colledger.colbill_item_dtl is '수금항목상세';

comment on column sbl_colledger.colbill_amt is '수금금액';

comment on column sbl_colledger.colbill_memo is '수금메모';

comment on column sbl_colledger.creator is '생성자';

comment on column sbl_colledger.create_dtime is '생성일시';

comment on column sbl_colledger.updater is '수정자';

comment on column sbl_colledger.update_dtime is '수정일시';

alter table sbl_colledger
    owner to ailis_user;

create table sbl_bank_deposit
(
    bank_deposit_id  varchar(50) not null
        constraint "PK_sbl_bank_deposit"
            primary key,
    account_div_cd   varchar(50),
    account_no       varchar(50),
    account_year     varchar(4),
    surecp_slstmt_no varchar(50),
    deposit_dt       date,
    deposit_amt      numeric,
    outamt           numeric,
    crcy_cd          varchar(50),
    remark           varchar(4000),
    reg_yn           boolean,
    creator          varchar(50) not null,
    create_dtime     timestamp   not null,
    updater          varchar(50) not null,
    update_dtime     timestamp   not null
);

comment on table sbl_bank_deposit is '은행입금정보';

comment on column sbl_bank_deposit.bank_deposit_id is '은행입금아이디';

comment on constraint "PK_sbl_bank_deposit" on sbl_bank_deposit is '은행입금정보 기본키';

comment on column sbl_bank_deposit.account_div_cd is '회계(계좌)구분코드';

comment on column sbl_bank_deposit.account_no is '회계(계좌)번호';

comment on column sbl_bank_deposit.account_year is '회계(계좌)년도';

comment on column sbl_bank_deposit.surecp_slstmt_no is '가수금전표번호';

comment on column sbl_bank_deposit.deposit_dt is '입금일자';

comment on column sbl_bank_deposit.deposit_amt is '입금금액';

comment on column sbl_bank_deposit.outamt is '미정산금액';

comment on column sbl_bank_deposit.crcy_cd is '통화(화폐)코드';

comment on column sbl_bank_deposit.remark is '비고(적요)';

comment on column sbl_bank_deposit.reg_yn is '등록여부';

comment on column sbl_bank_deposit.creator is '생성자';

comment on column sbl_bank_deposit.create_dtime is '생성일시';

comment on column sbl_bank_deposit.updater is '수정자';

comment on column sbl_bank_deposit.update_dtime is '수정일시';

alter table sbl_bank_deposit
    owner to ailis_user;

create table sbl_card_pay
(
    card_pay_id  varchar(50) not null
        constraint "PK_sbl_card_pay"
            primary key,
    shop_id      varchar(50) not null,
    trade_no     varchar(50) not null,
    card_bill_no varchar(50),
    card_comp_cd varchar(50),
    card_comp_nm varchar(50),
    card_no      varchar(50),
    instl_month  varchar(2),
    pay_amt      numeric,
    pay_dt       varchar(8),
    pay_time     varchar(6),
    card_appr_no varchar(50),
    pay_div_cd   varchar(50),
    reg_yn       boolean,
    creator      varchar(50) not null,
    create_dtime timestamp   not null,
    updater      varchar(50) not null,
    update_dtime timestamp   not null
);

comment on table sbl_card_pay is '신용카드결제정보';

comment on column sbl_card_pay.card_pay_id is '카드결제아이디';

comment on constraint "PK_sbl_card_pay" on sbl_card_pay is '신용카드결제정보 기본키';

comment on column sbl_card_pay.shop_id is '상점아이디';

comment on column sbl_card_pay.trade_no is '거래번호';

comment on column sbl_card_pay.card_bill_no is '카드계산서번호';

comment on column sbl_card_pay.card_comp_cd is '카드회사코드';

comment on column sbl_card_pay.card_comp_nm is '카드회사명';

comment on column sbl_card_pay.card_no is '카드번호';

comment on column sbl_card_pay.instl_month is '할부(개)월';

comment on column sbl_card_pay.pay_amt is '결제금액';

comment on column sbl_card_pay.pay_dt is '결제일자';

comment on column sbl_card_pay.pay_time is '결제시간';

comment on column sbl_card_pay.card_appr_no is '카드결재(승인)번호';

comment on column sbl_card_pay.pay_div_cd is '결제구분코드';

comment on column sbl_card_pay.reg_yn is '등록여부';

comment on column sbl_card_pay.creator is '생성자';

comment on column sbl_card_pay.create_dtime is '생성일시';

comment on column sbl_card_pay.updater is '수정자';

comment on column sbl_card_pay.update_dtime is '수정일시';

alter table sbl_card_pay
    owner to ailis_user;

create table sbl_estimate_dtl
(
    estimate_dtl_id varchar(50)       not null
        constraint "PK_sbl_estimate_dtl"
            primary key,
    estimate_id     varchar(50)       not null,
    seq             integer           not null,
    item            varchar(50)       not null,
    qnty            numeric default 0 not null,
    unit_price      numeric default 0 not null,
    supval          numeric default 0 not null,
    addtax          numeric default 0 not null,
    demand_charge   numeric default 0 not null,
    creator         varchar(50)       not null,
    create_dtime    timestamp         not null,
    updater         varchar(50)       not null,
    update_dtime    timestamp         not null
);

comment on table sbl_estimate_dtl is '견적서발행상세';

comment on column sbl_estimate_dtl.estimate_dtl_id is 'UUID';

comment on constraint "PK_sbl_estimate_dtl" on sbl_estimate_dtl is '견적서발행상세 기본키';

comment on column sbl_estimate_dtl.estimate_id is '견적서아이디';

comment on column sbl_estimate_dtl.seq is '순번';

comment on column sbl_estimate_dtl.item is '항목';

comment on column sbl_estimate_dtl.qnty is '수량';

comment on column sbl_estimate_dtl.unit_price is '단가';

comment on column sbl_estimate_dtl.supval is '공급가액';

comment on column sbl_estimate_dtl.addtax is '세액';

comment on column sbl_estimate_dtl.demand_charge is '청구가액';

comment on column sbl_estimate_dtl.creator is '생성자';

comment on column sbl_estimate_dtl.create_dtime is '생성일시';

comment on column sbl_estimate_dtl.updater is '수정자';

comment on column sbl_estimate_dtl.update_dtime is '수정일시';

alter table sbl_estimate_dtl
    owner to ailis_user;

create unique index sbl_estimate_dtl_unique
    on sbl_estimate_dtl (estimate_id, seq);

create table sbl_estimate
(
    estimate_id   varchar(50)       not null
        constraint "PK_sbl_estimate"
            primary key,
    publ_dt       date              not null,
    doc_title     varchar(100)      not null,
    doc_type_nm   varchar(50)       not null,
    doc_no        varchar(50)       not null,
    recp_person   varchar(50)       not null,
    ref_person    varchar(50),
    supval        numeric default 0 not null,
    addtax        numeric default 0 not null,
    demand_charge numeric default 0 not null,
    remark        varchar(4000),
    spnote        varchar(4000),
    worker        varchar(50),
    work_dept     varchar(50),
    creator       varchar(50)       not null,
    create_dtime  timestamp         not null,
    updater       varchar(50)       not null,
    update_dtime  timestamp         not null
);

comment on table sbl_estimate is '견적서발행내역';

comment on column sbl_estimate.estimate_id is 'UUID';

comment on constraint "PK_sbl_estimate" on sbl_estimate is '견적서발행내역 기본키';

comment on column sbl_estimate.publ_dt is '발행일';

comment on column sbl_estimate.doc_title is '문서제목';

comment on column sbl_estimate.doc_type_nm is '문서유형명';

comment on column sbl_estimate.doc_no is '문서번호';

comment on column sbl_estimate.recp_person is '수신자';

comment on column sbl_estimate.ref_person is '참조자';

comment on column sbl_estimate.supval is '공급가액';

comment on column sbl_estimate.addtax is '세액';

comment on column sbl_estimate.demand_charge is '청구가액';

comment on column sbl_estimate.remark is '비고';

comment on column sbl_estimate.spnote is '특이사항';

comment on column sbl_estimate.worker is '작업자';

comment on column sbl_estimate.work_dept is '작업부서';

comment on column sbl_estimate.creator is '생성자';

comment on column sbl_estimate.create_dtime is '생성일시';

comment on column sbl_estimate.updater is '수정자';

comment on column sbl_estimate.update_dtime is '수정일시';

alter table sbl_estimate
    owner to ailis_user;

create table sbl_sales_target
(
    sales_target_id           varchar(50)       not null
        constraint "PK_sbl_sales_target"
            primary key,
    cust_cd                   varchar(50)       not null,
    sales_year                varchar(4)        not null,
    sales_month               varchar(2)        not null,
    sals_team_cd              varchar(50)       not null,
    month_sales_target_amt    numeric default 0 not null,
    past_year_month_sales_amt numeric default 0 not null,
    creator                   varchar(50)       not null,
    create_dtime              timestamp         not null,
    updater                   varchar(50)       not null,
    update_dtime              timestamp         not null
);

comment on table sbl_sales_target is '고객매출목표';

comment on column sbl_sales_target.sales_target_id is 'UUID';

comment on constraint "PK_sbl_sales_target" on sbl_sales_target is '고객매출목표 기본키';

comment on column sbl_sales_target.cust_cd is '고객코드';

comment on column sbl_sales_target.sales_year is '매출년도';

comment on column sbl_sales_target.sales_month is '매출월';

comment on column sbl_sales_target.sals_team_cd is '영업팀코드';

comment on column sbl_sales_target.month_sales_target_amt is '월매출목표금액';

comment on column sbl_sales_target.past_year_month_sales_amt is '전년도월매출액';

comment on column sbl_sales_target.creator is '생성자';

comment on column sbl_sales_target.create_dtime is '생성일시';

comment on column sbl_sales_target.updater is '수정자';

comment on column sbl_sales_target.update_dtime is '수정일시';

alter table sbl_sales_target
    owner to ailis_user;

create table scs_gcgn_sals_pic_info_1
(
    row_number     bigint,
    cust_mst_id    varchar(50),
    cust_cd        varchar(50),
    apply_start_dt date,
    apply_end_dt   date,
    sals_team_cd   varchar(50),
    emp_user_id    varchar(50),
    creator        varchar(50),
    create_dtime   timestamp,
    updater        varchar(50),
    update_dtime   timestamp
);

alter table scs_gcgn_sals_pic_info_1
    owner to ailis_user;

create table scs_gcgn_sals_pic_info_kyt
(
    gcgn_sals_pic_info_id bigint,
    cust_mst_id           varchar(50),
    cust_cd               varchar(50),
    apply_start_dt        date,
    apply_end_dt          date,
    sals_team_cd          varchar(50),
    emp_user_id           varchar(50),
    creator               varchar(50),
    create_dtime          timestamp,
    updater               varchar(50),
    update_dtime          timestamp
);

alter table scs_gcgn_sals_pic_info_kyt
    owner to ailis_user;

create table sbl_colbill
(
    colbill_id       varchar(50)           not null
        constraint "PK_sbl_colbill"
            primary key,
    cust_cd          varchar(50)           not null,
    colbill_dt       date                  not null,
    pay_method_cd    varchar(50)           not null,
    card_comp_cd     varchar(50),
    card_comp_nm     varchar(50),
    pay_amt          numeric,
    card_appr_no     varchar(50),
    card_no          varchar(50),
    card_pay_id      varchar(50),
    card_bill_no     varchar(50),
    instl_month      varchar(2),
    bank_deposit_id  varchar(50),
    account_year     varchar(4),
    surecp_slstmt_no varchar(20),
    sales_slstmt_no  varchar(20),
    advrece_yn       boolean default false,
    closing_cd       varchar(50),
    send_yn          boolean default false not null,
    creator          varchar(50)           not null,
    create_dtime     timestamp             not null,
    updater          varchar(50)           not null,
    update_dtime     timestamp             not null,
    colledger_id     varchar(50)
);

comment on column sbl_colbill.colledger_id is '청구수금원장아이디';

alter table sbl_colbill
    owner to ailis_user;

create table scs_if_field_info_test
(
    if_field_info_id varchar(50) not null
        constraint "PK_scs_if_field_info_test"
            primary key,
    if_field_nm      varchar(100),
    if_field_col_nm  varchar(100),
    if_field_exps    varchar(500),
    if_field_desc    varchar(500),
    stat_cd          varchar(50),
    semantic_cd      varchar(50),
    ref_cd           varchar(50),
    creator          varchar(50) not null,
    create_dtime     timestamp   not null,
    updater          varchar(50) not null,
    update_dtime     timestamp   not null
);

comment on table scs_if_field_info_test is '연동필드정보(테스트)';

comment on column scs_if_field_info_test.if_field_info_id is '연동필드아이디';

comment on constraint "PK_scs_if_field_info_test" on scs_if_field_info_test is '연동필드정보(테스트) 기본키';

comment on column scs_if_field_info_test.if_field_nm is '연동필드명';

comment on column scs_if_field_info_test.if_field_col_nm is '연동필드열(컬럼)명';

comment on column scs_if_field_info_test.if_field_exps is '연동필드표현식';

comment on column scs_if_field_info_test.if_field_desc is '연동필드설명';

comment on column scs_if_field_info_test.stat_cd is '구분코드(환자/검체/의뢰/추가 등)';

comment on column scs_if_field_info_test.semantic_cd is '매핑코드(내부 매핑키)';

comment on column scs_if_field_info_test.ref_cd is '참조코드(규격코드)';

comment on column scs_if_field_info_test.creator is '생성자';

comment on column scs_if_field_info_test.create_dtime is '생성일시';

comment on column scs_if_field_info_test.updater is '수정자';

comment on column scs_if_field_info_test.update_dtime is '수정일시';

alter table scs_if_field_info_test
    owner to ailis_user;

