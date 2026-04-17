create table bbs_dept_grp_itm_tst
(
    dept_grp_itm_tst_id varchar(50) not null
        primary key,
    dept_cd             varchar(50) not null,
    tst_cate_cd         varchar(50) not null,
    tst_cate_item_cd    varchar(50) not null,
    tst_cd              varchar(50) not null,
    creator             varchar(50) not null,
    create_dtime        timestamp   not null,
    constraint bbs_dept_grp_itm_tst_unique
        unique (dept_cd, tst_cate_cd, tst_cate_item_cd, tst_cd)
);

comment on table bbs_dept_grp_itm_tst is '부서 검사분류 항목 (검사종목)';

comment on column bbs_dept_grp_itm_tst.dept_grp_itm_tst_id is 'UUID';

comment on column bbs_dept_grp_itm_tst.dept_cd is '부서코드';

comment on column bbs_dept_grp_itm_tst.tst_cate_cd is '검사분류코드';

comment on column bbs_dept_grp_itm_tst.tst_cate_item_cd is '검사분류 항목코드';

comment on column bbs_dept_grp_itm_tst.tst_cd is '검사코드';

comment on column bbs_dept_grp_itm_tst.creator is '생성자';

comment on column bbs_dept_grp_itm_tst.create_dtime is '생성일시';

alter table bbs_dept_grp_itm_tst
    owner to ailis_user;

create table bts_item_estl_doc
(
    item_estl_doc_id varchar(50) not null
        primary key,
    tst_cd           varchar(50) not null,
    doc_cd           varchar(50) not null,
    creator          varchar(50) not null,
    create_dtime     timestamp   not null,
    constraint bts_item_estl_doc_unique
        unique (tst_cd, doc_cd)
);

comment on table bts_item_estl_doc is '검사종목 (필수서류)';

comment on column bts_item_estl_doc.item_estl_doc_id is 'UUID';

comment on column bts_item_estl_doc.tst_cd is '검사코드';

comment on column bts_item_estl_doc.doc_cd is '서류코드';

comment on column bts_item_estl_doc.creator is '생성자';

comment on column bts_item_estl_doc.create_dtime is '생성일시';

alter table bts_item_estl_doc
    owner to ailis_user;

create table bts_item_gene
(
    item_gene_id varchar(50) not null
        primary key,
    tst_cd       varchar(50) not null,
    gene_cd      varchar(50) not null,
    creator      varchar(50) not null,
    create_dtime timestamp   not null,
    constraint bts_item_gene_unique
        unique (tst_cd, gene_cd)
);

comment on table bts_item_gene is '검사종목 (유전자)';

comment on column bts_item_gene.item_gene_id is 'UUID';

comment on column bts_item_gene.tst_cd is '검사코드';

comment on column bts_item_gene.gene_cd is '유전자 코드';

comment on column bts_item_gene.creator is '생성자';

comment on column bts_item_gene.create_dtime is '생성일시';

alter table bts_item_gene
    owner to ailis_user;

create index bts_item_gene_idx01
    on bts_item_gene (gene_cd, tst_cd);

create table bts_ref_item
(
    ref_item_id  varchar(50)           not null
        constraint bts_ref_item_id_pkey
            primary key,
    tst_cd       varchar(50)           not null,
    ref_cd       varchar(50)           not null,
    estl_yn      boolean default false not null,
    sort_order   integer,
    creator      varchar(50)           not null,
    create_dtime timestamp             not null,
    updater      varchar(50)           not null,
    update_dtime timestamp             not null,
    constraint bts_ref_item_unique
        unique (tst_cd, ref_cd)
);

comment on table bts_ref_item is '검사종목 (참조항목)';

comment on column bts_ref_item.ref_item_id is 'UUID';

comment on column bts_ref_item.tst_cd is '검사코드';

comment on column bts_ref_item.ref_cd is '참조항목';

comment on column bts_ref_item.estl_yn is '필수여부';

comment on column bts_ref_item.sort_order is '정렬순서';

comment on column bts_ref_item.creator is '생성자';

comment on column bts_ref_item.create_dtime is '생성일시';

comment on column bts_ref_item.updater is '수정자';

comment on column bts_ref_item.update_dtime is '수정일시';

create table bts_item_sub
(
    item_sub_id         varchar(50)                        not null
        primary key,
    tst_cd              varchar(50)                        not null,
    tst_sub_cd          varchar(50)                        not null,
    start_dt            date                               not null,
    end_dt              date    default '9999-12-31'::date not null,
    use_yn              boolean default true               not null,
    tst_sub_nm          varchar(200)                       not null,
    tst_sub_abbr_nm     varchar(50)                        not null,
    tst_sub_eng_nm      varchar(200)                       not null,
    tst_sub_eng_abbr_nm varchar(50)                        not null,
    tst_sub_int_nm      varchar(200),
    rst_type_short_yn   boolean                            not null,
    rst_type_long_yn    boolean                            not null,
    rst_type_file_yn    boolean                            not null,
    rst_type_url_yn     boolean                            not null,
    ref_val             varchar(1000),
    eng_ref_val         varchar(1000),
    creator             varchar(50)                        not null,
    create_dtime        timestamp                          not null,
    updater             varchar(50)                        not null,
    update_dtime        timestamp                          not null,
    constraint bts_item_sub__unique
        unique (tst_cd, tst_sub_cd)
);

comment on table bts_item_sub is '검사부속종목';

comment on column bts_item_sub.item_sub_id is 'UUID';

comment on column bts_item_sub.tst_cd is '부속코드';

comment on column bts_item_sub.tst_sub_cd is '검사부속코드';

comment on column bts_item_sub.start_dt is '시작일자';

comment on column bts_item_sub.end_dt is '종료일자';

comment on column bts_item_sub.use_yn is '사용여부';

comment on column bts_item_sub.tst_sub_nm is '부속코드명';

comment on column bts_item_sub.tst_sub_abbr_nm is '부속코드명_약어';

comment on column bts_item_sub.tst_sub_eng_nm is '부속코드명_영문';

comment on column bts_item_sub.tst_sub_eng_abbr_nm is '부속코드명_영문_약어';

comment on column bts_item_sub.tst_sub_int_nm is '부속코드명_내부';

comment on column bts_item_sub.rst_type_short_yn is '결과형태 단문 ';

comment on column bts_item_sub.rst_type_long_yn is '결과형태 장문 ';

comment on column bts_item_sub.rst_type_file_yn is '결과형태 파일';

comment on column bts_item_sub.rst_type_url_yn is '결과형태 URL';

comment on column bts_item_sub.ref_val is '참고치';

comment on column bts_item_sub.eng_ref_val is '참고치(영문)';

comment on column bts_item_sub.creator is '생성자';

comment on column bts_item_sub.create_dtime is '생성일시';

comment on column bts_item_sub.updater is '수정자';

comment on column bts_item_sub.update_dtime is '수정일시';

alter table bts_item_sub
    owner to ailis_user;


create table bts_item_sub_hst
(
    item_sub_hst_id     varchar(50)  not null
        primary key,
    hst_desc            varchar(200) not null,
    item_sub_id         varchar(50),
    tst_cd              varchar(50),
    tst_sub_cd          varchar(50),
    start_dt            date,
    end_dt              date,
    use_yn              boolean,
    tst_sub_nm          varchar(200),
    tst_sub_abbr_nm     varchar(50),
    tst_sub_eng_nm      varchar(200),
    tst_sub_eng_abbr_nm varchar(50),
    tst_sub_int_nm      varchar(200),
    rst_type_short_yn   boolean,
    rst_type_long_yn    boolean,
    rst_type_file_yn    boolean,
    rst_type_url_yn     boolean,
    ref_val             varchar(1000),
    eng_ref_val         varchar(1000),
    creator             varchar(50),
    create_dtime        timestamp,
    updater             varchar(50),
    update_dtime        timestamp
);

comment on table bts_item_sub_hst is '검사부속종목 (히스토리)';

comment on column bts_item_sub_hst.item_sub_hst_id is 'UUID';

comment on column bts_item_sub_hst.hst_desc is '변경사유';

comment on column bts_item_sub_hst.item_sub_id is 'UUID';

comment on column bts_item_sub_hst.tst_cd is '부속코드';

comment on column bts_item_sub_hst.tst_sub_cd is '검사부속코드';

comment on column bts_item_sub_hst.start_dt is '시작일자';

comment on column bts_item_sub_hst.end_dt is '종료일자';

comment on column bts_item_sub_hst.use_yn is '사용여부';

comment on column bts_item_sub_hst.tst_sub_nm is '부속코드명';

comment on column bts_item_sub_hst.tst_sub_abbr_nm is '부속코드명_약어';

comment on column bts_item_sub_hst.tst_sub_eng_nm is '부속코드명_영문';

comment on column bts_item_sub_hst.tst_sub_eng_abbr_nm is '부속코드명_영문_약어';

comment on column bts_item_sub_hst.tst_sub_int_nm is '부속코드명내부';

comment on column bts_item_sub_hst.rst_type_short_yn is '결과형태 단문 ';

comment on column bts_item_sub_hst.rst_type_long_yn is '결과형태 장문 ';

comment on column bts_item_sub_hst.rst_type_file_yn is '결과형태 파일';

comment on column bts_item_sub_hst.rst_type_url_yn is '결과형태 URL';

comment on column bts_item_sub_hst.ref_val is '참고치';

comment on column bts_item_sub_hst.eng_ref_val is '참고치(영문)';

comment on column bts_item_sub_hst.creator is '생성자';

comment on column bts_item_sub_hst.create_dtime is '생성일시';

comment on column bts_item_sub_hst.updater is '수정자';

comment on column bts_item_sub_hst.update_dtime is '수정일시';

alter table bts_item_sub_hst
    owner to ailis_user;


alter table bts_ref_item
    owner to ailis_user;

create table bbs_dept_group
(
    dept_group_id  varchar(50)                                not null
        primary key,
    dept_cd        varchar(50)                                not null,
    tst_cate_cd    varchar(50)                                not null,
    tst_cate_nm    varchar(100)                               not null,
    update_auth_cd varchar(50) default 'D'::character varying not null,
    dup_allow_yn   boolean                                    not null,
    sort_order     integer     default 0                      not null,
    creator        varchar(50)                                not null,
    create_dtime   timestamp                                  not null,
    updater        varchar(50)                                not null,
    update_dtime   timestamp                                  not null,
    constraint bbs_dept_group_unique
        unique (dept_cd, tst_cate_cd)
);

comment on table bbs_dept_group is '부서 검사분류';

comment on column bbs_dept_group.dept_group_id is 'UUID';

comment on column bbs_dept_group.dept_cd is '부서코드';

comment on column bbs_dept_group.tst_cate_cd is '검사분류코드';

comment on column bbs_dept_group.tst_cate_nm is '검사분류명';

comment on column bbs_dept_group.update_auth_cd is '수정권한';

comment on column bbs_dept_group.dup_allow_yn is '중복허용';

comment on column bbs_dept_group.sort_order is '정렬순서';

comment on column bbs_dept_group.creator is '생성자';

comment on column bbs_dept_group.create_dtime is '생성일시';

comment on column bbs_dept_group.updater is '수정자';

comment on column bbs_dept_group.update_dtime is '수정일시';

alter table bbs_dept_group
    owner to ailis_user;

create table bbs_dept_grp_itm
(
    dept_grp_itm_id  varchar(50)  not null
        primary key,
    dept_cd          varchar(50)  not null,
    tst_cate_cd      varchar(50)  not null,
    tst_cate_item_cd varchar(50)  not null,
    tst_cate_item_nm varchar(100) not null,
    sort_order       integer      not null,
    creator          varchar(50)  not null,
    create_dtime     timestamp    not null,
    updater          varchar(50)  not null,
    update_dtime     timestamp    not null,
    constraint bbs_dept_grp_itm_unique
        unique (dept_cd, tst_cate_cd, tst_cate_item_cd)
);

comment on table bbs_dept_grp_itm is '부서 검사분류 (항목)';

comment on column bbs_dept_grp_itm.dept_grp_itm_id is 'UUID';

comment on column bbs_dept_grp_itm.dept_cd is '부서코드';

comment on column bbs_dept_grp_itm.tst_cate_cd is '검사분류코드';

comment on column bbs_dept_grp_itm.tst_cate_item_cd is '검사분류 항목코드';

comment on column bbs_dept_grp_itm.tst_cate_item_nm is '검사분류 항목명';

comment on column bbs_dept_grp_itm.sort_order is '정렬순서';

comment on column bbs_dept_grp_itm.creator is '생성자';

comment on column bbs_dept_grp_itm.create_dtime is '생성일시';

comment on column bbs_dept_grp_itm.updater is '수정자';

comment on column bbs_dept_grp_itm.update_dtime is '수정일시';

alter table bbs_dept_grp_itm
    owner to ailis_user;

create table bbs_tst_req_doc
(
    doc_cd          varchar(50)  not null
        primary key,
    doc_div_cd      varchar(50)  not null,
    doc_nm          varchar(100) not null,
    doc_eng_nm      varchar(100) not null,
    doc_file_id     varchar(50)  not null,
    doc_eng_file_id varchar(50)  not null,
    creator         varchar(50)  not null,
    create_dtime    timestamp    not null,
    updater         varchar(50)  not null,
    update_dtime    timestamp    not null
);

comment on table bbs_tst_req_doc is '검사의뢰 서류';

comment on column bbs_tst_req_doc.doc_cd is '서류코드';

comment on column bbs_tst_req_doc.doc_div_cd is '서류구분';

comment on column bbs_tst_req_doc.doc_nm is '서류명';

comment on column bbs_tst_req_doc.doc_eng_nm is '서류명(영문)';

comment on column bbs_tst_req_doc.doc_file_id is '서류파일';

comment on column bbs_tst_req_doc.doc_eng_file_id is '서류파일(영문)';

comment on column bbs_tst_req_doc.creator is '생성자';

comment on column bbs_tst_req_doc.create_dtime is '생성일시';

comment on column bbs_tst_req_doc.updater is '수정자';

comment on column bbs_tst_req_doc.update_dtime is '수정일시';

alter table bbs_tst_req_doc
    owner to ailis_user;

create table bbs_tst_ref__
(
    ref_cd          varchar(50)           not null,
    ref_cate_cd     varchar(50),
    use_yn          boolean default true  not null,
    ref_nm          varchar(100)          not null,
    ref_abbr_nm     varchar(50)           not null,
    ref_eng_nm      varchar(100)          not null,
    ref_eng_abbr_nm varchar(50)           not null,
    sort_order      integer,
    ref_type        varchar(50)           not null,
    ref_size        integer               not null,
    range_chk_yn    boolean default false not null,
    ref_min_val     integer               not null,
    ref_max_val     integer               not null,
    data_format     varchar(100),
    dft_data        varchar(1000)         not null,
    creator         varchar(50)           not null,
    create_dtime    timestamp             not null,
    updater         varchar(50)           not null,
    update_detime   timestamp             not null
);

comment on table bbs_tst_ref__ is '검사 참조항목';

comment on column bbs_tst_ref__.ref_cd is '참조항목';

comment on column bbs_tst_ref__.ref_cate_cd is '분류';

comment on column bbs_tst_ref__.use_yn is '사용여부';

comment on column bbs_tst_ref__.ref_nm is '참조명';

comment on column bbs_tst_ref__.ref_abbr_nm is '참조명_약어';

comment on column bbs_tst_ref__.ref_eng_nm is '참조명(영어)';

comment on column bbs_tst_ref__.ref_eng_abbr_nm is '참조명_약어(영어)';

comment on column bbs_tst_ref__.sort_order is '정렬순서';

comment on column bbs_tst_ref__.ref_type is '데이터 타입';

comment on column bbs_tst_ref__.ref_size is '데이터 크기';

comment on column bbs_tst_ref__.range_chk_yn is '입력범위 확인';

comment on column bbs_tst_ref__.ref_min_val is '최소값';

comment on column bbs_tst_ref__.ref_max_val is '최대값';

comment on column bbs_tst_ref__.data_format is '포멧';

comment on column bbs_tst_ref__.dft_data is '기본값';

comment on column bbs_tst_ref__.creator is '생성자';

comment on column bbs_tst_ref__.create_dtime is '생성일시';

comment on column bbs_tst_ref__.updater is '수정자';

comment on column bbs_tst_ref__.update_detime is '수정일시';

alter table bbs_tst_ref__
    owner to ailis_user;

create table bbs_tst_ref_group_item
(
    tst_ref_group_item_id varchar(50) not null
        primary key,
    ref_group_cd          varchar(50) not null,
    ref_cd                varchar(50) not null,
    sort_order            integer,
    creator               varchar(50) not null,
    create_dtime          timestamp   not null,
    updater               varchar(50) not null,
    update_dtime          timestamp   not null,
    constraint bbs_tst_ref_group_item_unique
        unique (ref_group_cd, ref_cd)
);

comment on table bbs_tst_ref_group_item is '검사 참조그룹 항목';

comment on column bbs_tst_ref_group_item.tst_ref_group_item_id is 'UUID';

comment on column bbs_tst_ref_group_item.ref_group_cd is '참조그룹 코드';

comment on column bbs_tst_ref_group_item.ref_cd is '참조항목';

comment on column bbs_tst_ref_group_item.sort_order is '정렬순서';

comment on column bbs_tst_ref_group_item.creator is '생성자';

comment on column bbs_tst_ref_group_item.create_dtime is '생성일시';

comment on column bbs_tst_ref_group_item.updater is '수정자';

comment on column bbs_tst_ref_group_item.update_dtime is '수정일시';

alter table bbs_tst_ref_group_item
    owner to ailis_user;

create table bbs_dept_tst_item
(
    dept_tst_item_id varchar(50)                                not null
        primary key,
    dept_cd          varchar(50)                                not null,
    tst_cd           varchar(50)                                not null,
    dan_div_cd       varchar(50) default 'D'::character varying not null,
    tst_dayweek      char(7)     default 'YYYYYNN'::bpchar      not null,
    tst_tatday       integer                                    not null,
    dept_tst_desc    varchar(1000),
    creator          varchar(50)                                not null,
    create_dtime     timestamp                                  not null,
    updater          varchar(50)                                not null,
    update_dtime     timestamp                                  not null,
    constraint bbs_dept_tst_item_unique
        unique (dept_cd, tst_cd)
);

comment on table bbs_dept_tst_item is '부서 검사종목';

comment on column bbs_dept_tst_item.dept_tst_item_id is 'UUID';

comment on column bbs_dept_tst_item.dept_cd is '부서코드';

comment on column bbs_dept_tst_item.tst_cd is '검사코드';

comment on column bbs_dept_tst_item.dan_div_cd is '주야검사';

comment on column bbs_dept_tst_item.tst_dayweek is '검사요일';

comment on column bbs_dept_tst_item.tst_tatday is '검사소요일수';

comment on column bbs_dept_tst_item.dept_tst_desc is '설명';

comment on column bbs_dept_tst_item.creator is '생성자';

comment on column bbs_dept_tst_item.create_dtime is '생성일시';

comment on column bbs_dept_tst_item.updater is '수정자';

comment on column bbs_dept_tst_item.update_dtime is '수정일시';

alter table bbs_dept_tst_item
    owner to ailis_user;

create table bts_stnd_charge
(
    stnd_charge_id         varchar(50)                     not null
        primary key,
    tst_cd                 varchar(50)                     not null,
    apply_start_dt         date                            not null,
    apply_end_dt           date default '9999-12-31'::date not null,
    insu_cd                varchar(50),
    insu_cate_no           varchar(50),
    relat_value_point      numeric,
    insu_charge            numeric                         not null,
    qlad_charge            numeric                         not null,
    stnd_charge            numeric                         not null,
    lowest_charge          numeric                         not null,
    qlad_cd                varchar(50),
    relat_value_qlad_point numeric                         not null,
    output_insu_cd         varchar(50),
    total_qlad_charge      numeric                         not null,
    supval                 numeric                         not null,
    addtax                 numeric                         not null,
    creator                varchar(50)                     not null,
    create_dtime           timestamp                       not null,
    constraint bts_stnd_charge_unique
        unique (tst_cd, apply_start_dt)
);

comment on table bts_stnd_charge is '검사종목 (기준수가)';

comment on column bts_stnd_charge.stnd_charge_id is 'UUID';

comment on column bts_stnd_charge.tst_cd is '검사코드';

comment on column bts_stnd_charge.apply_start_dt is '수가시작일';

comment on column bts_stnd_charge.apply_end_dt is '수가종료일';

comment on column bts_stnd_charge.insu_cd is '보험코드';

comment on column bts_stnd_charge.insu_cate_no is '보험분류번호';

comment on column bts_stnd_charge.relat_value_point is '상대가치점수';

comment on column bts_stnd_charge.insu_charge is '보험수가';

comment on column bts_stnd_charge.qlad_charge is '질가산료';

comment on column bts_stnd_charge.stnd_charge is '기준수가';

comment on column bts_stnd_charge.lowest_charge is '최저수가';

comment on column bts_stnd_charge.qlad_cd is '질가산코드';

comment on column bts_stnd_charge.relat_value_qlad_point is '상대가치질가산점수';

comment on column bts_stnd_charge.output_insu_cd is '출력보험코드';

comment on column bts_stnd_charge.total_qlad_charge is '질가산료합';

comment on column bts_stnd_charge.supval is '공급액';

comment on column bts_stnd_charge.addtax is '부가세액';

comment on column bts_stnd_charge.creator is '생성자';

comment on column bts_stnd_charge.create_dtime is '생성일시';

alter table bts_stnd_charge
    owner to ailis_user;

create table bbs_tst_ref_group
(
    ref_group_cd    varchar(50)  not null
        primary key,
    ref_nm          varchar(200) not null,
    ref_abbr_nm     varchar(200),
    ref_eng_nm      varchar(200) not null,
    ref_eng_abbr_nm varchar(200),
    sort_order      integer,
    creator         varchar(50)  not null,
    create_dtime    timestamp    not null,
    updater         varchar(50)  not null,
    update_dtime    timestamp    not null
);

comment on table bbs_tst_ref_group is '검사 참조그룹';

comment on column bbs_tst_ref_group.ref_group_cd is '참조그룹 코드';

comment on column bbs_tst_ref_group.ref_nm is '참조그룹명';

comment on column bbs_tst_ref_group.ref_abbr_nm is '참조그룹명_약어';

comment on column bbs_tst_ref_group.ref_eng_nm is '참조그룹명(영문)';

comment on column bbs_tst_ref_group.ref_eng_abbr_nm is '참조그룹명_약어(영문)';

comment on column bbs_tst_ref_group.sort_order is '정렬순서';

comment on column bbs_tst_ref_group.creator is '생성자';

comment on column bbs_tst_ref_group.create_dtime is '생성일시';

comment on column bbs_tst_ref_group.updater is '수정자';

comment on column bbs_tst_ref_group.update_dtime is '수정일시';

alter table bbs_tst_ref_group
    owner to ailis_user;

create table bbs_tst_trn
(
    tst_trn_id   varchar(50) not null
        primary key,
    tst_req_dt   date        not null,
    tst_req_no   varchar(50) not null,
    tst_hst_cd   varchar(50) not null,
    hst_id       varchar(50) not null,
    creator      varchar(50) not null,
    create_dtime timestamp   not null
);

comment on table bbs_tst_trn is '검사 트랜잭션';

comment on column bbs_tst_trn.tst_trn_id is 'UUID';

comment on column bbs_tst_trn.tst_req_dt is '의뢰일자';

comment on column bbs_tst_trn.tst_req_no is '의뢰번호';

comment on column bbs_tst_trn.tst_hst_cd is '이력테이블 ID (TRNID)';

comment on column bbs_tst_trn.hst_id is '이력 UUID';

comment on column bbs_tst_trn.creator is '생성자';

comment on column bbs_tst_trn.create_dtime is '생성일시';

alter table bbs_tst_trn
    owner to ailis_user;

create index bbs_tst_trn_idx01
    on bbs_tst_trn (tst_req_dt, tst_req_no, create_dtime, tst_hst_cd);

create table bbs_spcm
(
    spcm_cd          varchar(50)  not null
        primary key,
    spcm_cate_cd     varchar(50),
    use_yn           boolean      not null,
    spcm_nm          varchar(100) not null,
    spcm_abbr_nm     varchar(50),
    spcm_eng_nm      varchar(100) not null
        constraint ascii_only_spcm_eng_nm
            check ((spcm_eng_nm)::text ~ '^[\x20-\x7E]*$'::text),
    spcm_eng_abbr_nm varchar(100)
        constraint ascii_only_spcm_eng_abbr_nm
            check ((spcm_eng_abbr_nm)::text ~ '^[\x20-\x7F]*$'::text),
    creator          varchar(50)  not null,
    create_dtime     timestamp    not null,
    updater          varchar(50)  not null,
    update_dtime     timestamp    not null
);

comment on table bbs_spcm is '검체';

comment on column bbs_spcm.spcm_cd is '검체코드';

comment on column bbs_spcm.spcm_cate_cd is '검체분류 (SPCM_CATE)';

comment on column bbs_spcm.use_yn is '사용여부';

comment on column bbs_spcm.spcm_nm is '검체명';

comment on column bbs_spcm.spcm_abbr_nm is '검체명_약어';

comment on column bbs_spcm.spcm_eng_nm is '검체명(영문)';

comment on column bbs_spcm.spcm_eng_abbr_nm is '검체명(영문)_약어';

comment on column bbs_spcm.creator is '생성자';

comment on column bbs_spcm.create_dtime is '생성일시';

comment on column bbs_spcm.updater is '수정자';

comment on column bbs_spcm.update_dtime is '수정일시';

alter table bbs_spcm
    owner to ailis_user;

create table bbs_tst_cate
(
    tst_cate_id        varchar(50)          not null
        primary key,
    tst_large_cate_cd  varchar(50)          not null,
    tst_medium_cate_cd varchar(50)          not null,
    cate_nm            varchar(200)         not null,
    cate_abbr_nm       varchar(50),
    cate_eng_nm        varchar(200)         not null
        constraint ascii_only_cate_eng_nm
            check ((cate_eng_nm)::text ~ '^[\x20-\x7E]*$'::text),
    cate_eng_abbr_nm   varchar(50)
        constraint ascii_only_cate_eng_abbr_nm
            check ((cate_eng_abbr_nm)::text ~ '^[\x20-\x7E]*$'::text),
    use_yn             boolean default true not null,
    sort_order         integer              not null,
    creator            varchar(50)          not null,
    create_dtime       timestamp            not null,
    updater            varchar(50)          not null,
    update_dtime       timestamp            not null,
    constraint bbs_tst_cate_unique
        unique (tst_large_cate_cd, tst_medium_cate_cd)
);

comment on table bbs_tst_cate is '검사분류';

comment on column bbs_tst_cate.tst_cate_id is 'UUID';

comment on column bbs_tst_cate.tst_large_cate_cd is '검사 대분류 코드';

comment on column bbs_tst_cate.tst_medium_cate_cd is '검사 중분류 코드';

comment on column bbs_tst_cate.cate_nm is '분류명';

comment on column bbs_tst_cate.cate_abbr_nm is '분류명_약어';

comment on column bbs_tst_cate.cate_eng_nm is '분류명_영문';

comment on column bbs_tst_cate.cate_eng_abbr_nm is '분류명_영문_약어';

comment on column bbs_tst_cate.use_yn is '사용여부';

comment on column bbs_tst_cate.sort_order is '정렬순서';

comment on column bbs_tst_cate.creator is '생성자';

comment on column bbs_tst_cate.create_dtime is '생성일시';

comment on column bbs_tst_cate.updater is '수정자';

comment on column bbs_tst_cate.update_dtime is '수정일시';

alter table bbs_tst_cate
    owner to ailis_user;

create table bbs_spcm_cntn
(
    spcm_cntn_cd varchar(50)  not null
        primary key,
    cntn_nm      varchar(100) not null,
    cntn_eng_nm  varchar(100) not null
        constraint ascii_only_cntn_eng_nm
            check ((cntn_eng_nm)::text ~ '^[\x20-\x7E]*$'::text),
    cntn_file_id varchar(50),
    creator      varchar(50)  not null,
    create_dtime timestamp    not null,
    updater      varchar(50)  not null,
    update_dtime timestamp    not null
);

comment on table bbs_spcm_cntn is '검체 용기';

comment on column bbs_spcm_cntn.spcm_cntn_cd is '검체용기코드';

comment on column bbs_spcm_cntn.cntn_nm is '용기명';

comment on column bbs_spcm_cntn.cntn_eng_nm is '용기명(영문)';

comment on column bbs_spcm_cntn.cntn_file_id is '용기이미지';

comment on column bbs_spcm_cntn.creator is '생성자';

comment on column bbs_spcm_cntn.create_dtime is '생성일시';

comment on column bbs_spcm_cntn.updater is '수정자';

comment on column bbs_spcm_cntn.update_dtime is '수정일시';

alter table bbs_spcm_cntn
    owner to ailis_user;

create table bbs_tst_ref
(
    ref_cd          varchar(50)           not null
        primary key,
    ref_cate_cd     varchar(50)           not null,
    use_yn          boolean default true  not null,
    ref_nm          varchar(100)          not null,
    ref_abbr_nm     varchar(50),
    ref_eng_nm      varchar(100)          not null
        constraint ascii_only_ref_eng_nm
            check ((ref_eng_nm)::text ~ '^[\x20-\x7E]*$'::text),
    ref_eng_abbr_nm varchar(50)
        constraint ascii_only_ref_eng_abbr_nm
            check ((ref_eng_abbr_nm)::text ~ '^[\x20-\x7E]*$'::text),
    sort_order      integer               not null,
    ref_type        varchar(50)           not null,
    ref_size        integer,
    range_chk_yn    boolean default false not null,
    ref_min_val     integer,
    ref_max_val     integer,
    data_format     varchar(100),
    dft_data        varchar(1000),
    dft_eng_data    varchar(1000)
        constraint ascii_only_dft_eng_data
            check ((dft_eng_data)::text ~ '^(?:[\t\x20-\x7E]|\r\n|\n|\r)*$'::text),
    creator         varchar(50)           not null,
    create_dtime    timestamp             not null,
    updater         varchar(50)           not null,
    update_dtime    timestamp             not null
);

comment on table bbs_tst_ref is '검사 참조항목';

comment on column bbs_tst_ref.ref_cd is '참조항목';

comment on column bbs_tst_ref.ref_cate_cd is '분류(REF_CATE)';

comment on column bbs_tst_ref.use_yn is '사용여부';

comment on column bbs_tst_ref.ref_nm is '참조명';

comment on column bbs_tst_ref.ref_abbr_nm is '참조명_약어';

comment on column bbs_tst_ref.ref_eng_nm is '참조명(영어)';

comment on column bbs_tst_ref.ref_eng_abbr_nm is '참조명_약어(영어)';

comment on column bbs_tst_ref.sort_order is '정렬순서';

comment on column bbs_tst_ref.ref_type is '데이터 타입';

comment on column bbs_tst_ref.ref_size is '데이터 크기';

comment on column bbs_tst_ref.range_chk_yn is '입력범위 확인';

comment on column bbs_tst_ref.ref_min_val is '최소값';

comment on column bbs_tst_ref.ref_max_val is '최대값';

comment on column bbs_tst_ref.data_format is '포멧';

comment on column bbs_tst_ref.dft_data is '기본값';

comment on column bbs_tst_ref.dft_eng_data is '기본값(영문)';

comment on column bbs_tst_ref.creator is '생성자';

comment on column bbs_tst_ref.create_dtime is '생성일시';

comment on column bbs_tst_ref.updater is '수정자';

comment on column bbs_tst_ref.update_dtime is '수정일시';

alter table bbs_tst_ref
    owner to ailis_user;

create table tbs_tst_report
(
    tst_report_id  varchar(50)           not null
        primary key,
    tst_req_dt     date                  not null,
    tst_req_no     bigint                not null,
    tst_cd         varchar(50)           not null,
    memo           varchar(500),
    lims_rcv_dtime timestamp,
    rst_short      text,
    rst_txt        text,
    atch_grup_id   varchar(50),
    rst_file_nm    varchar(200),
    rst_file_ext   varchar(50),
    rst_file_path  varchar(200),
    rst_url        varchar(200),
    delivery_yn    boolean default false not null,
    delivery_cd    varchar(50),
    delivery_dtime timestamp,
    deliverer      varchar(50),
    creator        varchar(50)           not null,
    create_dtime   timestamp             not null,
    updater        varchar(50)           not null,
    update_dtime   timestamp             not null,
    constraint tbs_tst_report_unique1
        unique (tst_req_dt, tst_req_no, tst_cd)
);

comment on table tbs_tst_report is '검사결과 (보고서)';

comment on column tbs_tst_report.tst_report_id is 'UUID';

comment on column tbs_tst_report.tst_req_dt is '의뢰일자';

comment on column tbs_tst_report.tst_req_no is '의뢰번호';

comment on column tbs_tst_report.tst_cd is '검사코드';

comment on column tbs_tst_report.memo is '이력메모';

comment on column tbs_tst_report.lims_rcv_dtime is 'LIMS수신일시';

comment on column tbs_tst_report.rst_short is '결과 단문';

comment on column tbs_tst_report.rst_txt is '결과 장문 ';

comment on column tbs_tst_report.atch_grup_id is '결과파일 id';

comment on column tbs_tst_report.rst_file_nm is '결과파일 이름';

comment on column tbs_tst_report.rst_file_ext is '결과파일 종류';

comment on column tbs_tst_report.rst_file_path is '결과파일 경로';

comment on column tbs_tst_report.rst_url is '결과 URL';

comment on column tbs_tst_report.delivery_yn is '배포여부';

comment on column tbs_tst_report.delivery_cd is '배포방식';

comment on column tbs_tst_report.delivery_dtime is '배포일시';

comment on column tbs_tst_report.deliverer is '배포자';

comment on column tbs_tst_report.creator is '생성자';

comment on column tbs_tst_report.create_dtime is '생성일시';

comment on column tbs_tst_report.updater is '수정자';

comment on column tbs_tst_report.update_dtime is '수정일시';

alter table tbs_tst_report
    owner to ailis_user;

create table tbs_tst_report_hst
(
    tst_report_hst_id varchar(50)  not null
        primary key,
    hst_cd            varchar(50)  not null,
    hst_memo          varchar(500) not null,
    worker            varchar(50)  not null,
    work_dtime        timestamp    not null,
    tst_req_dt        date         not null,
    tst_req_no        bigint       not null,
    tst_cd            varchar(50)  not null,
    memo              varchar(500),
    lims_rcv_dtime    timestamp,
    rst_short         text,
    rst_txt           text,
    atch_grup_id      varchar(50),
    rst_file_nm       varchar(200),
    rst_file_ext      varchar(50),
    rst_file_path     varchar(200),
    rst_url           varchar(200),
    delivery_yn       boolean      not null,
    delivery_cd       varchar(50),
    delivery_dtime    timestamp,
    deliverer         varchar(50),
    creator           varchar(50)  not null,
    create_dtime      timestamp    not null,
    updater           varchar(50)  not null,
    update_dtime      timestamp    not null
);

comment on table tbs_tst_report_hst is '검사결과 (보고서 이력)';

comment on column tbs_tst_report_hst.tst_report_hst_id is 'UUID';

comment on column tbs_tst_report_hst.hst_cd is '이력생성유형코드 HST';

comment on column tbs_tst_report_hst.hst_memo is '이력메모';

comment on column tbs_tst_report_hst.worker is '작업자';

comment on column tbs_tst_report_hst.work_dtime is '작업일시';

comment on column tbs_tst_report_hst.tst_req_dt is '의뢰일자';

comment on column tbs_tst_report_hst.tst_req_no is '의뢰번호';

comment on column tbs_tst_report_hst.tst_cd is '검사코드';

comment on column tbs_tst_report_hst.memo is '이력메모';

comment on column tbs_tst_report_hst.lims_rcv_dtime is 'LIMS수신일시';

comment on column tbs_tst_report_hst.rst_short is '결과 단문';

comment on column tbs_tst_report_hst.rst_txt is '결과 장문 ';

comment on column tbs_tst_report_hst.atch_grup_id is '결과파일 id';

comment on column tbs_tst_report_hst.rst_file_nm is '결과파일 이름';

comment on column tbs_tst_report_hst.rst_file_ext is '결과파일 종류';

comment on column tbs_tst_report_hst.rst_file_path is '결과파일 경로';

comment on column tbs_tst_report_hst.rst_url is '결과 URL';

comment on column tbs_tst_report_hst.delivery_yn is '배포여부';

comment on column tbs_tst_report_hst.delivery_cd is '배포방식';

comment on column tbs_tst_report_hst.delivery_dtime is '배포일시';

comment on column tbs_tst_report_hst.deliverer is '배포자';

comment on column tbs_tst_report_hst.creator is '생성자';

comment on column tbs_tst_report_hst.create_dtime is '생성일시';

comment on column tbs_tst_report_hst.updater is '수정자';

comment on column tbs_tst_report_hst.update_dtime is '수정일시';

alter table tbs_tst_report_hst
    owner to ailis_user;

create index tbs_tst_report_hst_idx01
    on tbs_tst_report_hst (tst_req_dt, tst_req_no, tst_cd, work_dtime);

create table bbs_gene
(
    gene_cd      varchar(50)       not null
        primary key,
    gene_nm      varchar(500),
    sort_order   integer default 0 not null,
    creator      varchar(50)       not null,
    create_dtime timestamp         not null,
    updater      varchar(50)       not null,
    update_dtime timestamp         not null
);

comment on table bbs_gene is '유전자';

comment on column bbs_gene.gene_cd is ' 유전자 코드';

comment on column bbs_gene.gene_nm is '유전자명';

comment on column bbs_gene.sort_order is '정렬순서';

comment on column bbs_gene.creator is '생성자';

comment on column bbs_gene.create_dtime is '생성일시';

comment on column bbs_gene.updater is '수정자';

comment on column bbs_gene.update_dtime is '수정일시';

alter table bbs_gene
    owner to ailis_user;

create index bbs_gene_idx01
    on bbs_gene (sort_order, gene_cd);

create index bbs_gene_idx02
    on bbs_gene (gene_nm);

create table bts_spcm
(
    spcm_id         varchar(50)          not null
        primary key,
    tst_cd          varchar(50)          not null,
    spcm_cd         varchar(50)          not null,
    sort_order      integer,
    estl_yn         boolean default true not null,
    take_qnty       varchar(100),
    eng_take_qnty   varchar(100),
    use_qnty        varchar(100),
    eng_use_qnty    varchar(100),
    strg_method     varchar(100),
    eng_strg_method varchar(100),
    spcm_stbl       varchar(1000),
    eng_spcm_stbl   varchar(1000),
    take_method     varchar(1000),
    eng_take_method varchar(1000),
    spcm_desc       varchar(1000),
    eng_desc        varchar(1000),
    caution         varchar(1000),
    eng_caution     varchar(1000),
    spcm_cntn_cd    varchar(50),
    creator         varchar(50)          not null,
    create_dtime    timestamp            not null,
    updater         varchar(50)          not null,
    update_dtime    timestamp            not null,
    constraint bts_spcm_unique
        unique (tst_cd, spcm_cd)
);

comment on table bts_spcm is '검사종목 (검체)';

comment on column bts_spcm.spcm_id is 'UUID';

comment on column bts_spcm.tst_cd is '검사코드';

comment on column bts_spcm.spcm_cd is '검체코드';

comment on column bts_spcm.sort_order is '정렬순서';

comment on column bts_spcm.estl_yn is '필수여부';

comment on column bts_spcm.take_qnty is '채취량';

comment on column bts_spcm.eng_take_qnty is '채취량(영문)';

comment on column bts_spcm.use_qnty is '소요량';

comment on column bts_spcm.eng_use_qnty is '소요량(영문)';

comment on column bts_spcm.strg_method is '보관방법';

comment on column bts_spcm.eng_strg_method is '보관방법(영문)';

comment on column bts_spcm.spcm_stbl is '검체안정성';

comment on column bts_spcm.eng_spcm_stbl is '검체안정성(영문)';

comment on column bts_spcm.take_method is '채취방법';

comment on column bts_spcm.eng_take_method is '채취방법(영문)';

comment on column bts_spcm.spcm_desc is '설명';

comment on column bts_spcm.eng_desc is '설명(영문)';

comment on column bts_spcm.caution is '주의사항';

comment on column bts_spcm.eng_caution is '주의사항(영문)';

comment on column bts_spcm.spcm_cntn_cd is '검체용기코드';

comment on column bts_spcm.creator is '생성자';

comment on column bts_spcm.create_dtime is '생성일시';

comment on column bts_spcm.updater is '수정자';

comment on column bts_spcm.update_dtime is '수정일시';

alter table bts_spcm
    owner to ailis_user;

create table bts_spcm_hst
(
    spcm_hst_id     varchar(50)          not null
        primary key,
    hst_desc        varchar(200),
    tst_cd          varchar(50)          not null,
    spcm_cd         varchar(50)          not null,
    sort_order      integer,
    estl_yn         boolean default true not null,
    take_qnty       varchar(100),
    eng_take_qnty   varchar(100),
    use_qnty        varchar(100),
    eng_use_qnty    varchar(100),
    strg_method     varchar(100),
    eng_strg_method varchar(100),
    spcm_stbl       varchar(1000),
    eng_spcm_stbl   varchar(1000),
    take_method     varchar(1000),
    eng_take_method varchar(1000),
    spcm_desc       varchar(1000),
    eng_desc        varchar(1000),
    caution         varchar(1000),
    eng_caution     varchar(1000),
    spcm_cntn_cd    varchar(50),
    creator         varchar(50)          not null,
    create_dtime    timestamp            not null,
    updater         varchar(50)          not null,
    update_dtime    timestamp            not null
);

comment on table bts_spcm_hst is '검사종목 (검체)';

comment on column bts_spcm_hst.spcm_hst_id is 'UUID';

comment on column bts_spcm_hst.hst_desc is '변경사유';

comment on column bts_spcm_hst.tst_cd is '검사코드';

comment on column bts_spcm_hst.spcm_cd is '검체코드';

comment on column bts_spcm_hst.sort_order is '정렬순서';

comment on column bts_spcm_hst.estl_yn is '필수여부';

comment on column bts_spcm_hst.take_qnty is '채취량';

comment on column bts_spcm_hst.eng_take_qnty is '채취량(영문)';

comment on column bts_spcm_hst.use_qnty is '소요량';

comment on column bts_spcm_hst.eng_use_qnty is '소요량(영문)';

comment on column bts_spcm_hst.strg_method is '보관방법';

comment on column bts_spcm_hst.eng_strg_method is '보관방법(영문)';

comment on column bts_spcm_hst.spcm_stbl is '검체안정성';

comment on column bts_spcm_hst.eng_spcm_stbl is '검체안정성(영문)';

comment on column bts_spcm_hst.take_method is '채취방법';

comment on column bts_spcm_hst.eng_take_method is '채취방법(영문)';

comment on column bts_spcm_hst.spcm_desc is '설명';

comment on column bts_spcm_hst.eng_desc is '설명(영문)';

comment on column bts_spcm_hst.caution is '주의사항';

comment on column bts_spcm_hst.eng_caution is '주의사항(영문)';

comment on column bts_spcm_hst.spcm_cntn_cd is '검체용기코드';

comment on column bts_spcm_hst.creator is '생성자';

comment on column bts_spcm_hst.create_dtime is '생성일시';

comment on column bts_spcm_hst.updater is '수정자';

comment on column bts_spcm_hst.update_dtime is '수정일시';

alter table bts_spcm_hst
    owner to ailis_user;

create table bts_item
(
    tst_cd             varchar(50)                        not null
        primary key,
    tst_large_cate_cd  varchar(50)                        not null,
    tst_medium_cate_cd varchar(50)                        not null,
    start_dt           date                               not null,
    end_dt             date    default '9999-12-31'::date not null,
    use_yn             boolean default true               not null,
    req_poss_yn        boolean default true               not null,
    web_kor_yn         boolean default true               not null,
    web_eng_yn         boolean default true               not null,
    amt_adj_yn         boolean default false              not null,
    tst_nm             varchar(200)                       not null,
    tst_abbr_nm        varchar(50)                        not null,
    tst_eng_nm         varchar(200)                       not null,
    tst_eng_abbr_nm    varchar(50)                        not null,
    tst_int_nm         varchar(200),
    rst_type_short_yn  boolean                            not null,
    rst_type_long_yn   boolean                            not null,
    rst_type_file_yn   boolean                            not null,
    rst_type_url_yn    boolean                            not null,
    disease_cd         varchar(50),
    tst_method_cd      varchar(50),
    ref_val            varchar(1000),
    eng_ref_val        varchar(1000),
    clnc_sgnf          varchar(4000),
    eng_clnc_sgnf      varchar(4000),
    tst_desc           varchar(4000),
    tst_eng_desc       varchar(4000),
    tst_dayweek        char(7),
    tst_tatday         integer,
    insu_apply_cd      varchar(50),
    insu_cd            varchar(50),
    insu_cate_no       varchar(50),
    tst_sub_yn         boolean default false              not null,
    creator            varchar(50)                        not null,
    create_dtime       timestamp                          not null,
    updater            varchar(50)                        not null,
    update_dtime       timestamp                          not null
);

comment on table bts_item is '검사종목';

comment on column bts_item.tst_cd is '검사코드';

comment on column bts_item.tst_large_cate_cd is '검사 대분류 코드';

comment on column bts_item.tst_medium_cate_cd is '검사 중분류 코드';

comment on column bts_item.start_dt is '시작일자';

comment on column bts_item.end_dt is '종료일자';

comment on column bts_item.use_yn is '사용여부';

comment on column bts_item.req_poss_yn is '의뢰가능여부';

comment on column bts_item.web_kor_yn is '한글 편람적용여부';

comment on column bts_item.web_eng_yn is '영문 편람적용여부';

comment on column bts_item.amt_adj_yn is '금액조정용여부';

comment on column bts_item.tst_nm is '검사명';

comment on column bts_item.tst_abbr_nm is '검사명_약어';

comment on column bts_item.tst_eng_nm is '검사명_영문';

comment on column bts_item.tst_eng_abbr_nm is '검사명_영문_약어';

comment on column bts_item.tst_int_nm is '검사명_내부';

comment on column bts_item.rst_type_short_yn is '결과형태 단문 ';

comment on column bts_item.rst_type_long_yn is '결과형태 장문 ';

comment on column bts_item.rst_type_file_yn is '결과형태 파일';

comment on column bts_item.rst_type_url_yn is '결과형태 URL';

comment on column bts_item.disease_cd is '질병코드';

comment on column bts_item.tst_method_cd is '검사방법코드';

comment on column bts_item.ref_val is '참고치';

comment on column bts_item.eng_ref_val is '참고치(영문)';

comment on column bts_item.clnc_sgnf is '임상적의의';

comment on column bts_item.eng_clnc_sgnf is '임상적의의(영문)';

comment on column bts_item.tst_desc is '검사설명';

comment on column bts_item.tst_eng_desc is '검사설명(영문)';

comment on column bts_item.tst_dayweek is '검사요일';

comment on column bts_item.tst_tatday is '검사소요일수';

comment on column bts_item.insu_apply_cd is '급여비급여구분';

comment on column bts_item.insu_cd is '보험코드';

comment on column bts_item.insu_cate_no is '보험분류번호';

comment on column bts_item.tst_sub_yn is '부속코드 여부';

comment on column bts_item.creator is '생성자';

comment on column bts_item.create_dtime is '생성일시';

comment on column bts_item.updater is '수정자';

comment on column bts_item.update_dtime is '수정일시';

alter table bts_item
    owner to ailis_user;

create index bts_item_idx01
    on bts_item (tst_large_cate_cd, tst_medium_cate_cd, tst_cd);

create table bts_item_hst
(
    item_hst_id        varchar(50)  not null
        primary key,
    hst_desc           varchar(200) not null,
    tst_cd             varchar(50),
    tst_large_cate_cd  varchar(50),
    tst_medium_cate_cd varchar(50),
    start_dt           date,
    end_dt             date,
    use_yn             boolean,
    req_poss_yn        boolean,
    web_kor_yn         boolean,
    web_eng_yn         boolean,
    amt_adj_yn         boolean,
    tst_nm             varchar(200),
    tst_abbr_nm        varchar(50),
    tst_eng_nm         varchar(200),
    tst_eng_abbr_nm    varchar(50),
    tst_int_nm         varchar(200),
    rst_type_short_yn  boolean,
    rst_type_long_yn   boolean,
    rst_type_file_yn   boolean,
    rst_type_url_yn    boolean,
    disease_cd         varchar(50),
    tst_method_cd      varchar(50),
    ref_val            varchar(1000),
    eng_ref_val        varchar(1000),
    clnc_sgnf          varchar(4000),
    eng_clnc_sgnf      varchar(4000),
    tst_desc           varchar(4000),
    tst_eng_desc       varchar(4000),
    tst_dayweek        char(7),
    tst_tatday         integer,
    insu_apply_cd      varchar(50),
    insu_cd            varchar(50),
    insu_cate_no       varchar(50),
    tst_sub_yn         boolean,
    creator            varchar(50)  not null,
    create_dtime       timestamp    not null,
    updater            varchar(50)  not null,
    update_dtime       timestamp    not null,
    constraint bts_item_hst_index01
        unique (tst_cd, item_hst_id)
);

comment on table bts_item_hst is '검사종목 (히스토리)';

comment on column bts_item_hst.item_hst_id is 'UUID';

comment on column bts_item_hst.hst_desc is '변경사유';

comment on column bts_item_hst.tst_cd is '검사코드';

comment on column bts_item_hst.tst_large_cate_cd is '검사 대분류 코드';

comment on column bts_item_hst.tst_medium_cate_cd is '검사 중분류 코드';

comment on column bts_item_hst.start_dt is '시작일자';

comment on column bts_item_hst.end_dt is '종료일자';

comment on column bts_item_hst.use_yn is '사용여부';

comment on column bts_item_hst.req_poss_yn is '의뢰가능여부';

comment on column bts_item_hst.web_kor_yn is '한글 편람적용여부';

comment on column bts_item_hst.web_eng_yn is '영문 편람적용여부';

comment on column bts_item_hst.tst_nm is '검사명';

comment on column bts_item_hst.tst_abbr_nm is '검사명_약어';

comment on column bts_item_hst.tst_eng_nm is '검사명_영문';

comment on column bts_item_hst.tst_eng_abbr_nm is '검사명_영문_약어';

comment on column bts_item_hst.tst_int_nm is '검사명_내부';

comment on column bts_item_hst.rst_type_short_yn is '결과형태 단문 ';

comment on column bts_item_hst.rst_type_long_yn is '결과형태 장문 ';

comment on column bts_item_hst.rst_type_file_yn is '결과형태 파일';

comment on column bts_item_hst.rst_type_url_yn is '결과형태 URL';

comment on column bts_item_hst.disease_cd is '질병코드';

comment on column bts_item_hst.tst_method_cd is '검사방법코드';

comment on column bts_item_hst.ref_val is '참고치';

comment on column bts_item_hst.eng_ref_val is '참고치(영문)';

comment on column bts_item_hst.clnc_sgnf is '임상적의의';

comment on column bts_item_hst.eng_clnc_sgnf is '임상적의의(영문)';

comment on column bts_item_hst.tst_desc is '검사설명';

comment on column bts_item_hst.tst_eng_desc is '검사설명(영문)';

comment on column bts_item_hst.tst_dayweek is '검사일';

comment on column bts_item_hst.tst_tatday is '검사소요일수';

comment on column bts_item_hst.insu_apply_cd is '급여비급여구분';

comment on column bts_item_hst.insu_cd is '보험코드';

comment on column bts_item_hst.insu_cate_no is '보험분류번호';

comment on column bts_item_hst.tst_sub_yn is '부속코드 여부';

comment on column bts_item_hst.creator is '생성자';

comment on column bts_item_hst.create_dtime is '생성일시';

comment on column bts_item_hst.updater is '수정자';

comment on column bts_item_hst.update_dtime is '수정일시';

alter table bts_item_hst
    owner to ailis_user;

create table rbs_amt_chg
(
    amt_chg_id          varchar(50) not null
        primary key,
    tst_req_dt          date        not null,
    tst_req_no          bigint      not null,
    tst_cd              varchar(50) not null,
    before_change_price numeric,
    after_change_price  numeric,
    price_change_cd     varchar(50),
    tst_item_set_yn     boolean default false,
    appr_info_no        bigint,
    appr_subms_emp_no   varchar(50),
    appr_subms_dtime    timestamp,
    curr_appr_seq       integer,
    last_appr_stat_cd   varchar(50),
    appr_lvl_cd         varchar(50),
    old_tst_req_dt      date,
    old_tst_req_no      bigint,
    remark              varchar(4000),
    creator             varchar(50) not null,
    create_dtime        timestamp   not null,
    updater             varchar(50) not null,
    update_dtime        timestamp   not null,
    constraint rbs_amt_chg_unique
        unique (tst_req_dt, tst_req_no, tst_cd)
);

comment on table rbs_amt_chg is '검사의뢰 (수가변경)';

comment on column rbs_amt_chg.amt_chg_id is 'UUID';

comment on column rbs_amt_chg.tst_req_dt is '의뢰일자';

comment on column rbs_amt_chg.tst_req_no is '의뢰번호';

comment on column rbs_amt_chg.tst_cd is '검사코드';

comment on column rbs_amt_chg.before_change_price is '변경전가격';

comment on column rbs_amt_chg.after_change_price is '변경후가격';

comment on column rbs_amt_chg.price_change_cd is '변경사유코드';

comment on column rbs_amt_chg.tst_item_set_yn is '의뢰금액 변경여부';

comment on column rbs_amt_chg.appr_info_no is '결재정보번호';

comment on column rbs_amt_chg.appr_subms_emp_no is '결재상신자사번';

comment on column rbs_amt_chg.appr_subms_dtime is '결재상신일시';

comment on column rbs_amt_chg.curr_appr_seq is '현재결재순번';

comment on column rbs_amt_chg.last_appr_stat_cd is '마지막결재상태코드';

comment on column rbs_amt_chg.appr_lvl_cd is '결재LEVEL코드';

comment on column rbs_amt_chg.old_tst_req_dt is '이전 의뢰일자';

comment on column rbs_amt_chg.old_tst_req_no is '이전 의뢰번호';

comment on column rbs_amt_chg.remark is '비고';

comment on column rbs_amt_chg.creator is '생성자';

comment on column rbs_amt_chg.create_dtime is '생성일시';

comment on column rbs_amt_chg.updater is '수정자';

comment on column rbs_amt_chg.update_dtime is '수정일시';

alter table rbs_amt_chg
    owner to ailis_user;

create table "rbs_doc__Dlt"
(
    doc_id       varchar(50) not null
        constraint rbs_doc_pkey
            primary key,
    tst_req_dt   date        not null,
    tst_req_no   bigint      not null,
    atch_grup_id varchar(50) not null,
    creator      varchar(50) not null,
    create_dtime timestamp   not null,
    constraint rbs_doc_unique
        unique (tst_req_dt, tst_req_no, atch_grup_id)
);

comment on table "rbs_doc__Dlt" is '검사의뢰 (의뢰문서)-삭제';

comment on column "rbs_doc__Dlt".doc_id is 'UUID';

comment on column "rbs_doc__Dlt".tst_req_dt is '의뢰일자';

comment on column "rbs_doc__Dlt".tst_req_no is '의뢰번호';

comment on column "rbs_doc__Dlt".atch_grup_id is '첨부파일';

comment on column "rbs_doc__Dlt".creator is '생성자';

comment on column "rbs_doc__Dlt".create_dtime is '생성일시';

alter table "rbs_doc__Dlt"
    owner to ailis_user;

create table rbs_patient
(
    patient_id        varchar(50)                                    not null
        primary key,
    tst_req_dt        date                                           not null,
    tst_req_no        bigint                                         not null,
    tst_req_path_cd   varchar(50),
    tst_req_div_cd    varchar(50),
    tst_req_stat_cd   varchar(50),
    pat_nm            varchar(100),
    pat_rrn1          varchar(50),
    pat_rrn2          varchar(50),
    pat_gender_cd     varchar(50),
    pat_bday          date,
    pat_age           integer,
    pat_email         varchar(100),
    direct_acct_cd    varchar(50)                                    not null,
    direct_acct_bar   varchar(50),
    cust_cd           varchar(50)                                    not null,
    hosp_chart_no     varchar(50),
    memo              varchar(100),
    report_lan_cd     varchar(50) default 'LG_KO'::character varying not null,
    remark            varchar(500),
    req_reg_yn        boolean     default false                      not null,
    agmt_reg_yn       boolean     default false                      not null,
    medi_sbjt_nm      varchar(100),
    dr_nm             varchar(100),
    sik_room          varchar(50),
    spcm_take_dtime   timestamp,
    spcm_cnt          integer,
    creator           varchar(50)                                    not null,
    create_dtime      timestamp                                      not null,
    updater           varchar(50)                                    not null,
    update_dtime      timestamp                                      not null,
    use_yn            boolean     default true,
    atch_file_grup_id varchar(50),
    constraint rbs_patient_unique
        unique (tst_req_dt, tst_req_no)
);

comment on table rbs_patient is '검사의뢰 (수진자)';

comment on column rbs_patient.patient_id is 'UUID';

comment on column rbs_patient.tst_req_dt is '의뢰일자';

comment on column rbs_patient.tst_req_no is '의뢰번호';

comment on column rbs_patient.tst_req_path_cd is '의뢰경로(RITP)';

comment on column rbs_patient.tst_req_div_cd is '의뢰구분(RQDV)';

comment on column rbs_patient.tst_req_stat_cd is '의뢰상태(RQST)';

comment on column rbs_patient.pat_nm is '수진자 이름';

comment on column rbs_patient.pat_rrn1 is '수진자 PN1';

comment on column rbs_patient.pat_rrn2 is '수진자 PN2';

comment on column rbs_patient.pat_gender_cd is '수진자 성별';

comment on column rbs_patient.pat_bday is '수진자 생일';

comment on column rbs_patient.pat_age is '수진자 나이';

comment on column rbs_patient.pat_email is '수진자 이메일';

comment on column rbs_patient.direct_acct_cd is '직접거래처 코드';

comment on column rbs_patient.direct_acct_bar is '직접거래처 바코드';

comment on column rbs_patient.cust_cd is '병원코드';

comment on column rbs_patient.hosp_chart_no is '차트번호';

comment on column rbs_patient.memo is '수진자메모';

comment on column rbs_patient.report_lan_cd is '결과지 언어 (LG)';

comment on column rbs_patient.remark is '비고';

comment on column rbs_patient.req_reg_yn is '의뢰서 등록여부';

comment on column rbs_patient.agmt_reg_yn is '동의서 등록여부';

comment on column rbs_patient.medi_sbjt_nm is '진료과목 명';

comment on column rbs_patient.dr_nm is '담당의사명';

comment on column rbs_patient.sik_room is '병동/병실';

comment on column rbs_patient.spcm_take_dtime is '검체채취일시';

comment on column rbs_patient.spcm_cnt is '검체수';

comment on column rbs_patient.creator is '생성자';

comment on column rbs_patient.create_dtime is '생성일시';

comment on column rbs_patient.updater is '수정자';

comment on column rbs_patient.update_dtime is '수정일시';

comment on column rbs_patient.use_yn is '사용여부';

comment on column rbs_patient.atch_file_grup_id is '첨부파일그룹아이디';

alter table rbs_patient
    owner to ailis_user;

create table rbs_patient_hst
(
    patient_hst_id    varchar(50)                                    not null
        primary key,
    hst_cd            varchar(50)                                    not null,
    hst_memo          varchar(500),
    worker            varchar(50)                                    not null,
    work_dtime        timestamp                                      not null,
    lims_apply_yn     boolean     default false                      not null,
    lims_apply_dtime  timestamp,
    tst_req_dt        date                                           not null,
    tst_req_no        bigint                                         not null,
    tst_req_path_cd   varchar(50),
    tst_req_div_cd    varchar(50),
    tst_req_stat_cd   varchar(50),
    pat_nm            varchar(100),
    pat_rrn1          varchar(50),
    pat_rrn2          varchar(50),
    pat_gender_cd     varchar(50),
    pat_bday          date,
    pat_age           integer,
    pat_email         varchar(100),
    direct_acct_cd    varchar(50)                                    not null,
    direct_acct_bar   varchar(50),
    cust_cd           varchar(50),
    hosp_chart_no     varchar(50),
    memo              varchar(100),
    report_lan_cd     varchar(50) default 'LG_KO'::character varying not null,
    remark            varchar(500),
    req_reg_yn        boolean     default false                      not null,
    agmt_reg_yn       boolean     default false                      not null,
    medi_sbjt_nm      varchar(100),
    dr_nm             varchar(100),
    sik_room          varchar(50),
    spcm_take_dtime   timestamp,
    spcm_cnt          integer,
    creator           varchar(50)                                    not null,
    create_dtime      timestamp                                      not null,
    updater           varchar(50)                                    not null,
    update_dtime      timestamp                                      not null,
    use_yn            boolean     default true,
    atch_file_grup_id varchar(50)
);

comment on table rbs_patient_hst is '검사의뢰 이력 (수진자)';

comment on column rbs_patient_hst.patient_hst_id is 'UUID';

comment on column rbs_patient_hst.hst_cd is '이력생성유형코드 HST';

comment on column rbs_patient_hst.hst_memo is '이력메모';

comment on column rbs_patient_hst.worker is '작업자';

comment on column rbs_patient_hst.work_dtime is '작업일시';

comment on column rbs_patient_hst.lims_apply_yn is 'LIMS적용여부';

comment on column rbs_patient_hst.lims_apply_dtime is 'LIMS적용일시';

comment on column rbs_patient_hst.tst_req_dt is '의뢰일자';

comment on column rbs_patient_hst.tst_req_no is '의뢰번호';

comment on column rbs_patient_hst.tst_req_path_cd is '의뢰경로(RITP)';

comment on column rbs_patient_hst.tst_req_div_cd is '의뢰구분(RQDV)';

comment on column rbs_patient_hst.tst_req_stat_cd is '의뢰상태(RQST)';

comment on column rbs_patient_hst.pat_nm is '수진자 이름';

comment on column rbs_patient_hst.pat_rrn1 is '수진자 PN1';

comment on column rbs_patient_hst.pat_rrn2 is '수진자 PN2';

comment on column rbs_patient_hst.pat_gender_cd is '수진자 성별';

comment on column rbs_patient_hst.pat_bday is '수진자 생일';

comment on column rbs_patient_hst.pat_age is '수진자 나이';

comment on column rbs_patient_hst.pat_email is '수진자 이메일';

comment on column rbs_patient_hst.direct_acct_cd is '직접거래처 코드';

comment on column rbs_patient_hst.direct_acct_bar is '직접거래처 바코드';

comment on column rbs_patient_hst.cust_cd is '병원코드';

comment on column rbs_patient_hst.hosp_chart_no is '차트번호';

comment on column rbs_patient_hst.memo is '수진자메모';

comment on column rbs_patient_hst.report_lan_cd is '결과지 언어 (LG)';

comment on column rbs_patient_hst.remark is '비고';

comment on column rbs_patient_hst.req_reg_yn is '의뢰서 등록여부';

comment on column rbs_patient_hst.agmt_reg_yn is '동의서 등록여부';

comment on column rbs_patient_hst.medi_sbjt_nm is '진료과목 명';

comment on column rbs_patient_hst.dr_nm is '담당의사명';

comment on column rbs_patient_hst.sik_room is '병동/병실';

comment on column rbs_patient_hst.spcm_take_dtime is '검체채취일시';

comment on column rbs_patient_hst.spcm_cnt is '검체수';

comment on column rbs_patient_hst.creator is '생성자';

comment on column rbs_patient_hst.create_dtime is '생성일시';

comment on column rbs_patient_hst.updater is '수정자';

comment on column rbs_patient_hst.update_dtime is '수정일시';

comment on column rbs_patient_hst.use_yn is '사용여부';

comment on column rbs_patient_hst.atch_file_grup_id is '첨부파일그룹아이디';

alter table rbs_patient_hst
    owner to ailis_user;

create index rbs_patient_hst_idx01
    on rbs_patient_hst (tst_req_dt, tst_req_no, work_dtime);

create table rbs_ref_item
(
    ref_item_id  varchar(50)  not null
        primary key,
    tst_req_dt   date         not null,
    tst_req_no   bigint       not null,
    ref_cd       varchar(50)  not null,
    ref_cont     varchar(500) not null,
    creator      varchar(50)  not null,
    create_dtime timestamp    not null,
    updater      varchar(50)  not null,
    update_dtime timestamp    not null,
    constraint rbs_ref_item_unique
        unique (tst_req_dt, tst_req_no, ref_cd)
);

comment on table rbs_ref_item is '검사의뢰 (참조항목)';

comment on column rbs_ref_item.ref_item_id is 'UUID';

comment on column rbs_ref_item.tst_req_dt is '의뢰일자';

comment on column rbs_ref_item.tst_req_no is '의뢰번호';

comment on column rbs_ref_item.ref_cd is '참조항목';

comment on column rbs_ref_item.ref_cont is '참조내용';

comment on column rbs_ref_item.creator is '생성자';

comment on column rbs_ref_item.create_dtime is '생성일시';

comment on column rbs_ref_item.updater is '수정자';

comment on column rbs_ref_item.update_dtime is '수정일시';

alter table rbs_ref_item
    owner to ailis_user;

create table rbs_ref_item_hst
(
    ref_item_hst_id  varchar(50)           not null
        primary key,
    hst_cd           varchar(50)           not null,
    hst_memo         varchar(500),
    worker           varchar(50)           not null,
    work_dtime       timestamp             not null,
    lims_apply_yn    boolean default false not null,
    lims_apply_dtime timestamp,
    tst_req_dt       date                  not null,
    tst_req_no       bigint                not null,
    ref_cd           varchar(50)           not null,
    ref_cont         varchar(500)          not null,
    creator          varchar(50)           not null,
    create_dtime     timestamp             not null,
    updater          varchar(50)           not null,
    update_dtime     timestamp             not null
);

comment on table rbs_ref_item_hst is '검사의뢰 이력 (참조항목)';

comment on column rbs_ref_item_hst.ref_item_hst_id is 'UUID';

comment on column rbs_ref_item_hst.hst_cd is '이력생성유형코드 HST';

comment on column rbs_ref_item_hst.hst_memo is '이력메모';

comment on column rbs_ref_item_hst.worker is '작업자';

comment on column rbs_ref_item_hst.work_dtime is '작업일시';

comment on column rbs_ref_item_hst.lims_apply_yn is 'LIMS적용여부';

comment on column rbs_ref_item_hst.lims_apply_dtime is 'LIMS적용일시';

comment on column rbs_ref_item_hst.tst_req_dt is '의뢰일자';

comment on column rbs_ref_item_hst.tst_req_no is '의뢰번호';

comment on column rbs_ref_item_hst.ref_cd is '참조항목';

comment on column rbs_ref_item_hst.ref_cont is '참조내용';

comment on column rbs_ref_item_hst.creator is '생성자';

comment on column rbs_ref_item_hst.create_dtime is '생성일시';

comment on column rbs_ref_item_hst.updater is '수정자';

comment on column rbs_ref_item_hst.update_dtime is '수정일시';

alter table rbs_ref_item_hst
    owner to ailis_user;

create index rbs_ref_item_hst_idx01
    on rbs_ref_item_hst (tst_req_dt, tst_req_no, work_dtime);

create table rbs_spcm
(
    spcm_id                varchar(50)           not null
        primary key,
    tst_req_dt             date                  not null,
    tst_req_no             bigint                not null,
    spcm_no                integer               not null,
    parent_tst_req_spcm_id varchar(50),
    spcm_cd                varchar(50)           not null,
    spcm_stat_cd           varchar(50)           not null,
    spcm_memo              varchar(200),
    spcm_take_dtime        timestamp,
    spcm_strg_dtime        timestamp             not null,
    remove_yn              boolean default false not null,
    rack_cd                varchar(50),
    rack_posit_row         integer,
    rack_posit_col         integer,
    creator                varchar(50)           not null,
    create_dtime           timestamp             not null,
    updater                varchar(50)           not null,
    update_dtime           timestamp             not null,
    constraint rbs_spcm_unique
        unique (tst_req_dt, tst_req_no, spcm_no)
);

comment on table rbs_spcm is '검사의뢰 (검체)';

comment on column rbs_spcm.spcm_id is 'UUID';

comment on column rbs_spcm.tst_req_dt is '의뢰일자';

comment on column rbs_spcm.tst_req_no is '의뢰번호';

comment on column rbs_spcm.spcm_no is '검체번호';

comment on column rbs_spcm.parent_tst_req_spcm_id is '상위검체 UUID';

comment on column rbs_spcm.spcm_cd is '검체코드';

comment on column rbs_spcm.spcm_stat_cd is '검체상태';

comment on column rbs_spcm.spcm_memo is '메모';

comment on column rbs_spcm.spcm_take_dtime is '검체채취일시';

comment on column rbs_spcm.spcm_strg_dtime is '검체입고일시';

comment on column rbs_spcm.remove_yn is '폐기여부';

comment on column rbs_spcm.rack_cd is '보관랙 코드';

comment on column rbs_spcm.rack_posit_row is '보관랙 위치 row';

comment on column rbs_spcm.rack_posit_col is '보관랙 위치 col';

comment on column rbs_spcm.creator is '생성자';

comment on column rbs_spcm.create_dtime is '생성일시';

comment on column rbs_spcm.updater is '수정자';

comment on column rbs_spcm.update_dtime is '수정일시';

alter table rbs_spcm
    owner to ailis_user;

create table rbs_spcm_hst
(
    spcm_hst_id            varchar(50)           not null
        primary key,
    hst_cd                 varchar(50)           not null,
    hst_memo               varchar(500),
    worker                 varchar(50)           not null,
    work_dtime             timestamp             not null,
    lims_apply_yn          boolean default false not null,
    lims_apply_dtime       timestamp,
    tst_req_dt             date                  not null,
    tst_req_no             bigint                not null,
    spcm_no                integer               not null,
    parent_tst_req_spcm_id varchar(50),
    spcm_cd                varchar(50)           not null,
    spcm_stat_cd           varchar(50)           not null,
    spcm_memo              varchar(200),
    spcm_take_dtime        timestamp,
    spcm_strg_dtime        timestamp             not null,
    remove_yn              boolean default false not null,
    rack_cd                varchar(50),
    rack_posit_row         integer,
    rack_posit_col         integer,
    creator                varchar(50)           not null,
    create_dtime           timestamp             not null,
    updater                varchar(50)           not null,
    update_dtime           timestamp             not null
);

comment on table rbs_spcm_hst is '검사의뢰 이력 (검체)';

comment on column rbs_spcm_hst.spcm_hst_id is 'UUID';

comment on column rbs_spcm_hst.hst_cd is '이력생성유형코드 HST';

comment on column rbs_spcm_hst.hst_memo is '이력메모';

comment on column rbs_spcm_hst.worker is '작업자';

comment on column rbs_spcm_hst.work_dtime is '작업일시';

comment on column rbs_spcm_hst.lims_apply_yn is 'LIMS적용여부';

comment on column rbs_spcm_hst.lims_apply_dtime is 'LIMS적용일시';

comment on column rbs_spcm_hst.tst_req_dt is '의뢰일자';

comment on column rbs_spcm_hst.tst_req_no is '의뢰번호';

comment on column rbs_spcm_hst.spcm_no is '검체번호';

comment on column rbs_spcm_hst.parent_tst_req_spcm_id is '상위검체 UUID';

comment on column rbs_spcm_hst.spcm_cd is '검체코드';

comment on column rbs_spcm_hst.spcm_stat_cd is '검체상태';

comment on column rbs_spcm_hst.spcm_memo is '메모';

comment on column rbs_spcm_hst.spcm_take_dtime is '검체채취일시';

comment on column rbs_spcm_hst.spcm_strg_dtime is '검체입고일시';

comment on column rbs_spcm_hst.remove_yn is '폐기여부';

comment on column rbs_spcm_hst.rack_cd is '보관랙 코드';

comment on column rbs_spcm_hst.rack_posit_row is '보관랙 위치 row';

comment on column rbs_spcm_hst.rack_posit_col is '보관랙 위치 col';

comment on column rbs_spcm_hst.creator is '생성자';

comment on column rbs_spcm_hst.create_dtime is '생성일시';

comment on column rbs_spcm_hst.updater is '수정자';

comment on column rbs_spcm_hst.update_dtime is '수정일시';

alter table rbs_spcm_hst
    owner to ailis_user;

create index rbs_spcm_hst_idx01
    on rbs_spcm_hst (tst_req_dt, tst_req_no, work_dtime);

create table rbs_tst_item
(
    tst_item_id               varchar(50)           not null
        primary key,
    tst_req_dt                date                  not null,
    tst_req_no                bigint                not null,
    tst_cd                    varchar(50)           not null,
    tst_req_path_cd           varchar(50),
    tst_req_div_cd            varchar(50),
    tst_req_stat_cd           varchar(50),
    spcm_cd                   varchar(50),
    emer_yn                   boolean default false not null,
    retst_num                 integer default 0     not null,
    tst_stat1_cd              varchar(50)           not null,
    tst_stat2_cd              varchar(50)           not null,
    tst_rst_type_id           varchar(50)           not null,
    insure_price              numeric default 0     not null,
    stnd_price                numeric default 0     not null,
    crcy_cd                   varchar(50)           not null,
    demand_charge             numeric default 0     not null,
    supval                    numeric default 0     not null,
    addtax                    numeric default 0     not null,
    demand_price_update_yn    boolean default false not null,
    demand_price_update_dtime timestamp,
    closing_cd                varchar(50),
    closing_dtime             timestamp,
    exrt_id                   bigint,
    closing_demand_charge     numeric,
    closing_supval            numeric,
    closing_addtax            numeric,
    charge_change_cd          varchar(50),
    closing_memo              varchar(500),
    closing_user              varchar(50),
    creator                   varchar(50)           not null,
    create_dtime              timestamp             not null,
    updater                   varchar(50)           not null,
    update_dtime              timestamp             not null,
    awb_no                    varchar(20),
    constraint rbs_tst_item_unique
        unique (tst_req_dt, tst_req_no, tst_cd)
);

comment on table rbs_tst_item is '검사의뢰 (검사종목)';

comment on column rbs_tst_item.tst_item_id is 'UUID';

comment on column rbs_tst_item.tst_req_dt is '의뢰일자';

comment on column rbs_tst_item.tst_req_no is '의뢰번호';

comment on column rbs_tst_item.tst_cd is '검사코드';

comment on column rbs_tst_item.tst_req_path_cd is '의뢰경로 (RITP)';

comment on column rbs_tst_item.tst_req_div_cd is '의뢰구분 (RQDV)';

comment on column rbs_tst_item.tst_req_stat_cd is '의뢰상태 (RQST)';

comment on column rbs_tst_item.spcm_cd is '검체코드';

comment on column rbs_tst_item.emer_yn is '응급여부';

comment on column rbs_tst_item.retst_num is '재검횟수';

comment on column rbs_tst_item.tst_stat1_cd is '검사상태1';

comment on column rbs_tst_item.tst_stat2_cd is '검사상태2';

comment on column rbs_tst_item.tst_rst_type_id is '결과지 타입ID';

comment on column rbs_tst_item.insure_price is '보험수가';

comment on column rbs_tst_item.stnd_price is '기준가';

comment on column rbs_tst_item.crcy_cd is '통화코드';

comment on column rbs_tst_item.demand_charge is '청구수가';

comment on column rbs_tst_item.supval is '공급가액';

comment on column rbs_tst_item.addtax is '부가세';

comment on column rbs_tst_item.demand_price_update_yn is '청구액수정여부';

comment on column rbs_tst_item.demand_price_update_dtime is '청구액수정일시';

comment on column rbs_tst_item.closing_cd is '마감코드';

comment on column rbs_tst_item.closing_dtime is '마감일시';

comment on column rbs_tst_item.exrt_id is '환율ID';

comment on column rbs_tst_item.closing_demand_charge is '청구수가(마감)';

comment on column rbs_tst_item.closing_supval is '공급가액(마감)';

comment on column rbs_tst_item.closing_addtax is '부가세(마감)';

comment on column rbs_tst_item.charge_change_cd is '수가변경 코드';

comment on column rbs_tst_item.closing_memo is '마감메모';

comment on column rbs_tst_item.closing_user is '마감자';

comment on column rbs_tst_item.creator is '생성자';

comment on column rbs_tst_item.create_dtime is '생성일시';

comment on column rbs_tst_item.updater is '수정자';

comment on column rbs_tst_item.update_dtime is '수정일시';

comment on column rbs_tst_item.awb_no is 'awb번호';

alter table rbs_tst_item
    owner to ailis_user;

create table rbs_tst_item_hst
(
    tst_item_hst_id           varchar(50)           not null
        primary key,
    hst_cd                    varchar(50)           not null,
    hst_memo                  varchar(500),
    worker                    varchar(50)           not null,
    work_dtime                timestamp             not null,
    lims_apply_yn             boolean default false not null,
    lims_apply_dtime          timestamp,
    tst_req_dt                date                  not null,
    tst_req_no                bigint                not null,
    tst_cd                    varchar(50)           not null,
    tst_req_path_cd           varchar(50),
    tst_req_div_cd            varchar(50),
    tst_req_stat_cd           varchar(50),
    spcm_cd                   varchar(50),
    emer_yn                   boolean default false not null,
    retst_num                 integer default 0     not null,
    tst_stat1_cd              varchar(50)           not null,
    tst_stat2_cd              varchar(50)           not null,
    tst_rst_type_id           varchar(50)           not null,
    insure_price              numeric default 0     not null,
    stnd_price                numeric default 0     not null,
    crcy_cd                   varchar(50)           not null,
    demand_charge             numeric default 0     not null,
    supval                    numeric default 0     not null,
    addtax                    numeric default 0     not null,
    demand_price_update_yn    boolean default false not null,
    demand_price_update_dtime timestamp,
    closing_cd                varchar(50),
    closing_dtime             timestamp,
    exrt_id                   bigint,
    closing_demand_charge     numeric,
    closing_supval            numeric,
    closing_addtax            numeric,
    charge_change_cd          varchar(50),
    closing_memo              varchar(500),
    closing_user              varchar(50),
    creator                   varchar(50)           not null,
    create_dtime              timestamp             not null,
    updater                   varchar(50)           not null,
    update_dtime              timestamp             not null,
    awb_no                    varchar(20)
);

comment on table rbs_tst_item_hst is '검사의뢰 이력 (검사종목)';

comment on column rbs_tst_item_hst.tst_item_hst_id is 'UUID';

comment on column rbs_tst_item_hst.hst_cd is '이력생성유형코드 HST';

comment on column rbs_tst_item_hst.hst_memo is '이력메모';

comment on column rbs_tst_item_hst.worker is '작업자';

comment on column rbs_tst_item_hst.work_dtime is '작업일시';

comment on column rbs_tst_item_hst.lims_apply_yn is 'LIMS적용여부';

comment on column rbs_tst_item_hst.lims_apply_dtime is 'LIMS적용일시';

comment on column rbs_tst_item_hst.tst_req_dt is '의뢰일자';

comment on column rbs_tst_item_hst.tst_req_no is '의뢰번호';

comment on column rbs_tst_item_hst.tst_cd is '검사코드';

comment on column rbs_tst_item_hst.tst_req_path_cd is '의뢰경로 (RITP)';

comment on column rbs_tst_item_hst.tst_req_div_cd is '의뢰구분 (RQDV)';

comment on column rbs_tst_item_hst.tst_req_stat_cd is '의뢰상태 (RQST)';

comment on column rbs_tst_item_hst.spcm_cd is '검체코드';

comment on column rbs_tst_item_hst.emer_yn is '응급여부';

comment on column rbs_tst_item_hst.retst_num is '재검횟수';

comment on column rbs_tst_item_hst.tst_stat1_cd is '검사상태1';

comment on column rbs_tst_item_hst.tst_stat2_cd is '검사상태2';

comment on column rbs_tst_item_hst.tst_rst_type_id is '결과지 타입ID';

comment on column rbs_tst_item_hst.insure_price is '보험수가';

comment on column rbs_tst_item_hst.stnd_price is '기준가';

comment on column rbs_tst_item_hst.crcy_cd is '통화코드';

comment on column rbs_tst_item_hst.demand_charge is '청구수가';

comment on column rbs_tst_item_hst.supval is '공급가액';

comment on column rbs_tst_item_hst.addtax is '부가세';

comment on column rbs_tst_item_hst.demand_price_update_yn is '청구액수정여부';

comment on column rbs_tst_item_hst.demand_price_update_dtime is '청구액수정일시';

comment on column rbs_tst_item_hst.closing_cd is '마감코드';

comment on column rbs_tst_item_hst.closing_dtime is '마감일시';

comment on column rbs_tst_item_hst.exrt_id is '환율ID';

comment on column rbs_tst_item_hst.closing_demand_charge is '청구수가(마감)';

comment on column rbs_tst_item_hst.closing_supval is '공급가액(마감)';

comment on column rbs_tst_item_hst.closing_addtax is '부가세(마감)';

comment on column rbs_tst_item_hst.charge_change_cd is '수가변경 코드';

comment on column rbs_tst_item_hst.closing_memo is '마감메모';

comment on column rbs_tst_item_hst.closing_user is '마감자';

comment on column rbs_tst_item_hst.creator is '생성자';

comment on column rbs_tst_item_hst.create_dtime is '생성일시';

comment on column rbs_tst_item_hst.updater is '수정자';

comment on column rbs_tst_item_hst.update_dtime is '수정일시';

comment on column rbs_tst_item_hst.awb_no is 'awb번호';

alter table rbs_tst_item_hst
    owner to ailis_user;

create index rbs_tst_item_hst_idx01
    on rbs_tst_item_hst (tst_req_dt, tst_req_no, work_dtime);

create table rbs_tst_trn
(
    tst_trn_id      varchar(50) not null
        primary key,
    tst_req_dt      date        not null,
    tst_req_no      bigint      not null,
    tst_req_stat_cd varchar(50) not null,
    patient_hst_id  varchar(50) not null,
    creator         varchar(50) not null,
    create_dtime    timestamp   not null
);

comment on table rbs_tst_trn is '의뢰상태 트랜잭션';

comment on column rbs_tst_trn.tst_trn_id is 'UUID';

comment on column rbs_tst_trn.tst_req_dt is '의뢰일자';

comment on column rbs_tst_trn.tst_req_no is '의뢰번호';

comment on column rbs_tst_trn.tst_req_stat_cd is '상태';

comment on column rbs_tst_trn.patient_hst_id is '이력 UUID';

comment on column rbs_tst_trn.creator is '생성자';

comment on column rbs_tst_trn.create_dtime is '생성일시';

alter table rbs_tst_trn
    owner to ailis_user;

create index rbs_tst_trn_idx01
    on rbs_tst_trn (tst_req_dt, tst_req_no, create_dtime);

create table rup_excel_info
(
    excel_info_id    varchar(50)  not null
        primary key,
    if_id            varchar(50)  not null,
    cust_cd          varchar(50)  not null,
    req_create_yn    boolean      not null,
    req_create_dtime timestamp,
    req_creator_id   varchar(50),
    upload_file_nm   varchar(200) not null,
    upload_file_path varchar(200) not null,
    save_file_nm     varchar(200) not null,
    upload_dtime     timestamp    not null,
    upload_user_id   varchar(50)  not null,
    file_ext         varchar(30)  not null,
    file_size        integer      not null,
    creator          varchar(50)  not null,
    create_dtime     timestamp    not null,
    updater          varchar(50)  not null,
    update_dtime     timestamp    not null
);

comment on table rup_excel_info is '엑셀 (파일정보)';

comment on column rup_excel_info.excel_info_id is 'UUID';

comment on column rup_excel_info.if_id is '연동아이디';

comment on column rup_excel_info.cust_cd is '고객코드';

comment on column rup_excel_info.req_create_yn is '의뢰생성여부';

comment on column rup_excel_info.req_create_dtime is '의뢰생성일시';

comment on column rup_excel_info.req_creator_id is '의뢰생성자아이디';

comment on column rup_excel_info.upload_file_nm is '업로드파일명';

comment on column rup_excel_info.upload_file_path is '업로드파일경로';

comment on column rup_excel_info.save_file_nm is '저장파일명';

comment on column rup_excel_info.upload_dtime is '업로드일시';

comment on column rup_excel_info.upload_user_id is '업로드사용자아이디';

comment on column rup_excel_info.file_ext is '파일확장자';

comment on column rup_excel_info.file_size is '파일사이즈';

comment on column rup_excel_info.creator is '생성자';

comment on column rup_excel_info.create_dtime is '생성일시';

comment on column rup_excel_info.updater is '수정자';

comment on column rup_excel_info.update_dtime is '수정일시';

alter table rup_excel_info
    owner to ailis_user;

create table rup_excel_req_info
(
    excel_req_info_id   varchar(50)  not null
        primary key,
    upload_file_info_id varchar(50)  not null,
    if_id               varchar(50)  not null,
    req_create_yn       boolean      not null,
    req_create_dtime    timestamp,
    req_creator_id      varchar(50),
    tst_req_dt          date         not null,
    tst_req_no          varchar(50)  not null,
    cust_cd             varchar(50)  not null,
    direct_acct_cd      varchar(50)  not null,
    medi_sbjt_cd        varchar(30)  not null,
    medi_sbjt_nm        varchar(100) not null,
    pat_nm              varchar(100) not null,
    pat_age             integer      not null,
    pat_gender_cd       varchar(50)  not null,
    pat_rrn1            varchar(6)   not null,
    pat_rrn2            varchar(7),
    pat_rrn3            varchar(14),
    pat_rrn4            varchar(13)  not null,
    pat_rrn5            varchar(50)  not null,
    hosp_chart_no       varchar(50)  not null,
    sik_room            varchar(50)  not null,
    dr_nm               varchar(100) not null,
    memo                varchar(100) not null,
    spcm_take_dtime     timestamp,
    tst_cd              varchar(50),
    spcm_cd             varchar(50)  not null,
    cust_tst_cd         varchar(30)  not null,
    cust_tst_nm         varchar(200) not null,
    etc_data1           varchar(50)  not null,
    etc_data2           varchar(50)  not null,
    etc_data3           varchar(50)  not null,
    etc_data4           varchar(50)  not null,
    etc_data5           varchar(50)  not null,
    reg_div_cd          varchar(50)  not null,
    hosp_barcode_no     varchar(80)  not null,
    if_seq              bigint,
    creator             varchar(50)  not null,
    create_dtime        timestamp    not null,
    updater             varchar(50)  not null,
    update_dtime        timestamp    not null
);

comment on table rup_excel_req_info is '엑셀 (의뢰정보)';

comment on column rup_excel_req_info.excel_req_info_id is 'UUID';

comment on column rup_excel_req_info.upload_file_info_id is '업로드파일아이디';

comment on column rup_excel_req_info.if_id is '연동아이디';

comment on column rup_excel_req_info.req_create_yn is '의뢰생성여부';

comment on column rup_excel_req_info.req_create_dtime is '의뢰생성일시';

comment on column rup_excel_req_info.req_creator_id is '의뢰생성자아이디';

comment on column rup_excel_req_info.tst_req_dt is '의뢰일자';

comment on column rup_excel_req_info.tst_req_no is '의뢰번호';

comment on column rup_excel_req_info.cust_cd is '고객코드';

comment on column rup_excel_req_info.direct_acct_cd is '직접거래처코드';

comment on column rup_excel_req_info.medi_sbjt_cd is '진료과코드';

comment on column rup_excel_req_info.medi_sbjt_nm is '진료과목 명';

comment on column rup_excel_req_info.pat_nm is '수진자명';

comment on column rup_excel_req_info.pat_age is '나이';

comment on column rup_excel_req_info.pat_gender_cd is '성별';

comment on column rup_excel_req_info.pat_rrn1 is '주민번호1';

comment on column rup_excel_req_info.pat_rrn2 is '주민번호2';

comment on column rup_excel_req_info.pat_rrn3 is '주민번호3';

comment on column rup_excel_req_info.pat_rrn4 is '주민번호4';

comment on column rup_excel_req_info.pat_rrn5 is '주민번호5';

comment on column rup_excel_req_info.hosp_chart_no is '차트번호';

comment on column rup_excel_req_info.sik_room is '병동';

comment on column rup_excel_req_info.dr_nm is '담당의사명';

comment on column rup_excel_req_info.memo is '수진자메모';

comment on column rup_excel_req_info.spcm_take_dtime is '검체체취일시';

comment on column rup_excel_req_info.tst_cd is '검사코드';

comment on column rup_excel_req_info.spcm_cd is '검체코드';

comment on column rup_excel_req_info.cust_tst_cd is '고객검사코드';

comment on column rup_excel_req_info.cust_tst_nm is '고객검사명';

comment on column rup_excel_req_info.etc_data1 is '기타자료1';

comment on column rup_excel_req_info.etc_data2 is '기타자료2';

comment on column rup_excel_req_info.etc_data3 is '기타자료3';

comment on column rup_excel_req_info.etc_data4 is '기타자료4';

comment on column rup_excel_req_info.etc_data5 is '기타자료5';

comment on column rup_excel_req_info.reg_div_cd is '등록구분코드';

comment on column rup_excel_req_info.hosp_barcode_no is '병원바코드번호';

comment on column rup_excel_req_info.if_seq is '연동일련번호';

comment on column rup_excel_req_info.creator is '생성자';

comment on column rup_excel_req_info.create_dtime is '생성일시';

comment on column rup_excel_req_info.updater is '수정자';

comment on column rup_excel_req_info.update_dtime is '수정일시';

alter table rup_excel_req_info
    owner to ailis_user;

create table rup_link_info
(
    link_info_id           varchar(50)  not null
        constraint rup_file_info_pkey
            primary key,
    link_cd                varchar(50)  not null,
    reqdte                 varchar(8)   not null,
    reqno                  varchar(50)  not null,
    reqno_short            varchar(50)  not null,
    patnm                  varchar(50)  not null,
    hosno                  varchar(200) not null,
    idno                   varchar(50)  not null,
    hosplc                 varchar(50)  not null,
    scdev                  varchar(10)  not null,
    sampcd                 varchar(50)  not null,
    hosloc                 varchar(50)  not null,
    urivol                 varchar(50)  not null,
    reqtme                 varchar(50)  not null,
    prgweek                varchar(50)  not null,
    mngno                  varchar(20)  not null,
    advyn                  varchar(50)  not null,
    emegyn                 varchar(1)   not null,
    samdte                 varchar(8)   not null,
    docnm                  varchar(30)  not null,
    itemcd                 varchar(50)  not null,
    canyn                  varchar(50)  not null,
    cstcd                  varchar(5)   not null,
    cstnm                  varchar(150) not null,
    busno                  varchar(10)  not null,
    clicd                  varchar(8)   not null,
    brccd                  varchar(3)   not null,
    brcnm                  varchar(100) not null,
    empno                  varchar(150) not null,
    empnm                  varchar(50)  not null,
    mobile                 varchar(30)  not null,
    clabregdate            date         not null,
    clabregno              integer      not null,
    ccompcode              varchar(50)  not null,
    clogordercode          varchar(30)  not null,
    clogtestname           varchar(150) not null,
    cisregistorder         char         not null,
    cisregistordertime     timestamp    not null,
    cregistmemberid        varchar(30)  not null,
    huploadtime            timestamp    not null,
    itmamt                 money        not null,
    issyncregist           char         not null,
    labsservernotfind      bit          not null,
    stepri                 money        not null,
    sampleno               varchar(100) not null,
    height                 varchar(50)  not null,
    weight                 varchar(50)  not null,
    digest_milk            varchar(50)  not null,
    desease_obesity        varchar(50)  not null,
    desease_diabetes       varchar(50)  not null,
    desease_hypertensive   varchar(50)  not null,
    desease_hyperlipidemia varchar(50)  not null,
    idx_activity           varchar(50)  not null,
    fetus                  varchar(50)  not null,
    race                   varchar(50)  not null,
    sonogram_week          varchar(50)  not null,
    sonogram_day           varchar(50)  not null,
    ivf                    varchar(50)  not null,
    sampnm                 varchar(50)  not null,
    barcode                varchar(100) not null,
    isretest               varchar(10)  not null,
    retestreqno            varchar(50)  not null,
    ultrasound_feature     varchar(50)  not null,
    multiple_marker        varchar(50)  not null,
    nuchal_translucency    varchar(50)  not null,
    sex                    char         not null,
    age                    integer      not null,
    memo                   varchar(100) not null,
    nursingnumber          varchar(50)  not null,
    sampleid               varchar(15)  not null,
    registrationnumber     varchar(50)  not null,
    birth_day              varchar(8)   not null,
    creator                varchar(50)  not null,
    create_dtime           timestamp    not null
);

comment on table rup_link_info is 'LAB,RMS 연동정보';

comment on column rup_link_info.link_info_id is 'UUID';

comment on column rup_link_info.link_cd is '연동유형 cate_cd = RITP	';

comment on column rup_link_info.reqdte is 'Labs의뢰일';

comment on column rup_link_info.reqno is 'Labs바코드번호';

comment on column rup_link_info.reqno_short is 'Labs바코드단축번호';

comment on column rup_link_info.patnm is '수진자명';

comment on column rup_link_info.hosno is '차트번호';

comment on column rup_link_info.idno is '주민번호';

comment on column rup_link_info.hosplc is '진료과명';

comment on column rup_link_info.scdev is 'Labs시검여부';

comment on column rup_link_info.sampcd is '검체코드';

comment on column rup_link_info.hosloc is '병동/병실';

comment on column rup_link_info.urivol is 'urivol';

comment on column rup_link_info.reqtme is '의뢰시간';

comment on column rup_link_info.prgweek is '임신주수';

comment on column rup_link_info.mngno is 'mngno';

comment on column rup_link_info.advyn is 'advyn';

comment on column rup_link_info.emegyn is 'emegyn';

comment on column rup_link_info.samdte is '검체체취일';

comment on column rup_link_info.docnm is '담당의사명';

comment on column rup_link_info.itemcd is 'LABS검사코드';

comment on column rup_link_info.canyn is 'canyn';

comment on column rup_link_info.cstcd is 'LABS고객코드';

comment on column rup_link_info.cstnm is 'LABS고객명';

comment on column rup_link_info.busno is '사업자번호';

comment on column rup_link_info.clicd is '요양기관번호';

comment on column rup_link_info.brccd is '영업소코드';

comment on column rup_link_info.brcnm is '영업소명';

comment on column rup_link_info.empno is '등록자사번';

comment on column rup_link_info.empnm is '등록자명';

comment on column rup_link_info.mobile is '등록자연락처';

comment on column rup_link_info.clabregdate is '지놈의뢰일자';

comment on column rup_link_info.clabregno is '지놈의뢰번호';

comment on column rup_link_info.ccompcode is '고객코드';

comment on column rup_link_info.clogordercode is '검사코드';

comment on column rup_link_info.clogtestname is '검사명';

comment on column rup_link_info.cisregistorder is '의뢰등록여부';

comment on column rup_link_info.cisregistordertime is '의뢰등록일시';

comment on column rup_link_info.cregistmemberid is '의뢰등록자';

comment on column rup_link_info.huploadtime is '연동일시';

comment on column rup_link_info.itmamt is 'itmamt';

comment on column rup_link_info.issyncregist is 'IsSyncRegist';

comment on column rup_link_info.labsservernotfind is 'LabsServerNotFind';

comment on column rup_link_info.stepri is 'stepri';

comment on column rup_link_info.sampleno is 'sampleno';

comment on column rup_link_info.height is '신장';

comment on column rup_link_info.weight is '체중';

comment on column rup_link_info.digest_milk is 'digest_milk';

comment on column rup_link_info.desease_obesity is 'desease_obesity';

comment on column rup_link_info.desease_diabetes is 'desease_diabetes';

comment on column rup_link_info.desease_hypertensive is 'desease_hypertensive';

comment on column rup_link_info.desease_hyperlipidemia is 'desease_hyperlipidemia';

comment on column rup_link_info.idx_activity is 'idx_activity';

comment on column rup_link_info.fetus is '태아';

comment on column rup_link_info.race is 'race';

comment on column rup_link_info.sonogram_week is 'sonogram_week';

comment on column rup_link_info.sonogram_day is 'sonogram_day';

comment on column rup_link_info.ivf is 'ivf';

comment on column rup_link_info.sampnm is 'sampnm';

comment on column rup_link_info.barcode is 'barcode';

comment on column rup_link_info.isretest is 'isretest';

comment on column rup_link_info.retestreqno is 'retestreqno';

comment on column rup_link_info.ultrasound_feature is 'ultrasound_feature';

comment on column rup_link_info.multiple_marker is 'multiple_marker';

comment on column rup_link_info.nuchal_translucency is 'nuchal_translucency';

comment on column rup_link_info.sex is '성별';

comment on column rup_link_info.age is '나이';

comment on column rup_link_info.memo is '의뢰메모';

comment on column rup_link_info.nursingnumber is '간호번호';

comment on column rup_link_info.sampleid is '바코드번호';

comment on column rup_link_info.registrationnumber is 'registrationNumber';

comment on column rup_link_info.birth_day is '생일';

comment on column rup_link_info.creator is '생성자';

comment on column rup_link_info.create_dtime is '생성일시';

alter table rup_link_info
    owner to ailis_user;

