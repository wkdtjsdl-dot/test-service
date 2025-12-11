create table tst_scm.bbs_dept_grp_itm_tst
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

comment on table tst_scm.bbs_dept_grp_itm_tst is '부서 검사분류 항목 (검사종목)';

comment on column tst_scm.bbs_dept_grp_itm_tst.dept_grp_itm_tst_id is 'UUID';

comment on column tst_scm.bbs_dept_grp_itm_tst.dept_cd is '부서코드';

comment on column tst_scm.bbs_dept_grp_itm_tst.tst_cate_cd is '검사분류코드';

comment on column tst_scm.bbs_dept_grp_itm_tst.tst_cate_item_cd is '검사분류 항목코드';

comment on column tst_scm.bbs_dept_grp_itm_tst.tst_cd is '검사코드';

comment on column tst_scm.bbs_dept_grp_itm_tst.creator is '생성자';

comment on column tst_scm.bbs_dept_grp_itm_tst.create_dtime is '생성일시';

alter table tst_scm.bbs_dept_grp_itm_tst
    owner to ailis_user;

create table tst_scm.bts_item_estl_doc
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

comment on table tst_scm.bts_item_estl_doc is '검사종목 (필수서류)';

comment on column tst_scm.bts_item_estl_doc.item_estl_doc_id is 'UUID';

comment on column tst_scm.bts_item_estl_doc.tst_cd is '검사코드';

comment on column tst_scm.bts_item_estl_doc.doc_cd is '서류코드';

comment on column tst_scm.bts_item_estl_doc.creator is '생성자';

comment on column tst_scm.bts_item_estl_doc.create_dtime is '생성일시';

alter table tst_scm.bts_item_estl_doc
    owner to ailis_user;

create table tst_scm.bts_item_gene
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

comment on table tst_scm.bts_item_gene is '검사종목 (유전자)';

comment on column tst_scm.bts_item_gene.item_gene_id is 'UUID';

comment on column tst_scm.bts_item_gene.tst_cd is '검사코드';

comment on column tst_scm.bts_item_gene.gene_cd is '유전자 코드';

comment on column tst_scm.bts_item_gene.creator is '생성자';

comment on column tst_scm.bts_item_gene.create_dtime is '생성일시';

alter table tst_scm.bts_item_gene
    owner to ailis_user;

create index bts_item_gene_idx01
    on tst_scm.bts_item_gene (gene_cd, tst_cd);

create table tst_scm.bts_ref_item
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

comment on table tst_scm.bts_ref_item is '검사종목 (참조항목)';

comment on column tst_scm.bts_ref_item.ref_item_id is 'UUID';

comment on column tst_scm.bts_ref_item.tst_cd is '검사코드';

comment on column tst_scm.bts_ref_item.ref_cd is '참조항목';

comment on column tst_scm.bts_ref_item.estl_yn is '필수여부';

comment on column tst_scm.bts_ref_item.sort_order is '정렬순서';

comment on column tst_scm.bts_ref_item.creator is '생성자';

comment on column tst_scm.bts_ref_item.create_dtime is '생성일시';

comment on column tst_scm.bts_ref_item.updater is '수정자';

comment on column tst_scm.bts_ref_item.update_dtime is '수정일시';

alter table tst_scm.bts_ref_item
    owner to ailis_user;

create table tst_scm.bts_spcm
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
    strg_method_cd  varchar(50),
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

comment on table tst_scm.bts_spcm is '검사종목 (검체)';

comment on column tst_scm.bts_spcm.spcm_id is 'UUID';

comment on column tst_scm.bts_spcm.tst_cd is '검사코드';

comment on column tst_scm.bts_spcm.spcm_cd is '검체코드';

comment on column tst_scm.bts_spcm.sort_order is '정렬순서';

comment on column tst_scm.bts_spcm.estl_yn is '필수여부';

comment on column tst_scm.bts_spcm.take_qnty is '채취량';

comment on column tst_scm.bts_spcm.eng_take_qnty is '채취량(영문)';

comment on column tst_scm.bts_spcm.use_qnty is '소요량';

comment on column tst_scm.bts_spcm.eng_use_qnty is '소요량(영문)';

comment on column tst_scm.bts_spcm.strg_method_cd is '보관방법';

comment on column tst_scm.bts_spcm.spcm_stbl is '검체안정성';

comment on column tst_scm.bts_spcm.eng_spcm_stbl is '검체안정성(영문)';

comment on column tst_scm.bts_spcm.take_method is '채취방법';

comment on column tst_scm.bts_spcm.eng_take_method is '채취방법(영문)';

comment on column tst_scm.bts_spcm.spcm_desc is '설명';

comment on column tst_scm.bts_spcm.eng_desc is '설명(영문)';

comment on column tst_scm.bts_spcm.caution is '주의사항';

comment on column tst_scm.bts_spcm.eng_caution is '주의사항(영문)';

comment on column tst_scm.bts_spcm.spcm_cntn_cd is '검체용기코드';

comment on column tst_scm.bts_spcm.creator is '생성자';

comment on column tst_scm.bts_spcm.create_dtime is '생성일시';

comment on column tst_scm.bts_spcm.updater is '수정자';

comment on column tst_scm.bts_spcm.update_dtime is '수정일시';

alter table tst_scm.bts_spcm
    owner to ailis_user;

create table tst_scm.bbs_dept_group
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

comment on table tst_scm.bbs_dept_group is '부서 검사분류';

comment on column tst_scm.bbs_dept_group.dept_group_id is 'UUID';

comment on column tst_scm.bbs_dept_group.dept_cd is '부서코드';

comment on column tst_scm.bbs_dept_group.tst_cate_cd is '검사분류코드';

comment on column tst_scm.bbs_dept_group.tst_cate_nm is '검사분류명';

comment on column tst_scm.bbs_dept_group.update_auth_cd is '수정권한';

comment on column tst_scm.bbs_dept_group.dup_allow_yn is '중복허용';

comment on column tst_scm.bbs_dept_group.sort_order is '정렬순서';

comment on column tst_scm.bbs_dept_group.creator is '생성자';

comment on column tst_scm.bbs_dept_group.create_dtime is '생성일시';

comment on column tst_scm.bbs_dept_group.updater is '수정자';

comment on column tst_scm.bbs_dept_group.update_dtime is '수정일시';

alter table tst_scm.bbs_dept_group
    owner to ailis_user;

create table tst_scm.bbs_dept_grp_itm
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

comment on table tst_scm.bbs_dept_grp_itm is '부서 검사분류 (항목)';

comment on column tst_scm.bbs_dept_grp_itm.dept_grp_itm_id is 'UUID';

comment on column tst_scm.bbs_dept_grp_itm.dept_cd is '부서코드';

comment on column tst_scm.bbs_dept_grp_itm.tst_cate_cd is '검사분류코드';

comment on column tst_scm.bbs_dept_grp_itm.tst_cate_item_cd is '검사분류 항목코드';

comment on column tst_scm.bbs_dept_grp_itm.tst_cate_item_nm is '검사분류 항목명';

comment on column tst_scm.bbs_dept_grp_itm.sort_order is '정렬순서';

comment on column tst_scm.bbs_dept_grp_itm.creator is '생성자';

comment on column tst_scm.bbs_dept_grp_itm.create_dtime is '생성일시';

comment on column tst_scm.bbs_dept_grp_itm.updater is '수정자';

comment on column tst_scm.bbs_dept_grp_itm.update_dtime is '수정일시';

alter table tst_scm.bbs_dept_grp_itm
    owner to ailis_user;

create table tst_scm.bbs_tst_req_doc
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

comment on table tst_scm.bbs_tst_req_doc is '검사의뢰 서류';

comment on column tst_scm.bbs_tst_req_doc.doc_cd is '서류코드';

comment on column tst_scm.bbs_tst_req_doc.doc_div_cd is '서류구분';

comment on column tst_scm.bbs_tst_req_doc.doc_nm is '서류명';

comment on column tst_scm.bbs_tst_req_doc.doc_eng_nm is '서류명(영문)';

comment on column tst_scm.bbs_tst_req_doc.doc_file_id is '서류파일';

comment on column tst_scm.bbs_tst_req_doc.doc_eng_file_id is '서류파일(영문)';

comment on column tst_scm.bbs_tst_req_doc.creator is '생성자';

comment on column tst_scm.bbs_tst_req_doc.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_req_doc.updater is '수정자';

comment on column tst_scm.bbs_tst_req_doc.update_dtime is '수정일시';

alter table tst_scm.bbs_tst_req_doc
    owner to ailis_user;

create table tst_scm.bbs_tst_ref__
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

comment on table tst_scm.bbs_tst_ref__ is '검사 참조항목';

comment on column tst_scm.bbs_tst_ref__.ref_cd is '참조항목';

comment on column tst_scm.bbs_tst_ref__.ref_cate_cd is '분류';

comment on column tst_scm.bbs_tst_ref__.use_yn is '사용여부';

comment on column tst_scm.bbs_tst_ref__.ref_nm is '참조명';

comment on column tst_scm.bbs_tst_ref__.ref_abbr_nm is '참조명_약어';

comment on column tst_scm.bbs_tst_ref__.ref_eng_nm is '참조명(영어)';

comment on column tst_scm.bbs_tst_ref__.ref_eng_abbr_nm is '참조명_약어(영어)';

comment on column tst_scm.bbs_tst_ref__.sort_order is '정렬순서';

comment on column tst_scm.bbs_tst_ref__.ref_type is '데이터 타입';

comment on column tst_scm.bbs_tst_ref__.ref_size is '데이터 크기';

comment on column tst_scm.bbs_tst_ref__.range_chk_yn is '입력범위 확인';

comment on column tst_scm.bbs_tst_ref__.ref_min_val is '최소값';

comment on column tst_scm.bbs_tst_ref__.ref_max_val is '최대값';

comment on column tst_scm.bbs_tst_ref__.data_format is '포멧';

comment on column tst_scm.bbs_tst_ref__.dft_data is '기본값';

comment on column tst_scm.bbs_tst_ref__.creator is '생성자';

comment on column tst_scm.bbs_tst_ref__.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_ref__.updater is '수정자';

comment on column tst_scm.bbs_tst_ref__.update_detime is '수정일시';

alter table tst_scm.bbs_tst_ref__
    owner to ailis_user;

create table tst_scm.bbs_tst_ref_group_item
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

comment on table tst_scm.bbs_tst_ref_group_item is '검사 참조그룹 항목';

comment on column tst_scm.bbs_tst_ref_group_item.tst_ref_group_item_id is 'UUID';

comment on column tst_scm.bbs_tst_ref_group_item.ref_group_cd is '참조그룹 코드';

comment on column tst_scm.bbs_tst_ref_group_item.ref_cd is '참조항목';

comment on column tst_scm.bbs_tst_ref_group_item.sort_order is '정렬순서';

comment on column tst_scm.bbs_tst_ref_group_item.creator is '생성자';

comment on column tst_scm.bbs_tst_ref_group_item.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_ref_group_item.updater is '수정자';

comment on column tst_scm.bbs_tst_ref_group_item.update_dtime is '수정일시';

alter table tst_scm.bbs_tst_ref_group_item
    owner to ailis_user;

create table tst_scm.bbs_dept_tst_item
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

comment on table tst_scm.bbs_dept_tst_item is '부서 검사종목';

comment on column tst_scm.bbs_dept_tst_item.dept_tst_item_id is 'UUID';

comment on column tst_scm.bbs_dept_tst_item.dept_cd is '부서코드';

comment on column tst_scm.bbs_dept_tst_item.tst_cd is '검사코드';

comment on column tst_scm.bbs_dept_tst_item.dan_div_cd is '주야검사';

comment on column tst_scm.bbs_dept_tst_item.tst_dayweek is '검사요일';

comment on column tst_scm.bbs_dept_tst_item.tst_tatday is '검사소요일수';

comment on column tst_scm.bbs_dept_tst_item.dept_tst_desc is '설명';

comment on column tst_scm.bbs_dept_tst_item.creator is '생성자';

comment on column tst_scm.bbs_dept_tst_item.create_dtime is '생성일시';

comment on column tst_scm.bbs_dept_tst_item.updater is '수정자';

comment on column tst_scm.bbs_dept_tst_item.update_dtime is '수정일시';

alter table tst_scm.bbs_dept_tst_item
    owner to ailis_user;

create table tst_scm.bts_stnd_charge
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

comment on table tst_scm.bts_stnd_charge is '검사종목 (기준수가)';

comment on column tst_scm.bts_stnd_charge.stnd_charge_id is 'UUID';

comment on column tst_scm.bts_stnd_charge.tst_cd is '검사코드';

comment on column tst_scm.bts_stnd_charge.apply_start_dt is '수가시작일';

comment on column tst_scm.bts_stnd_charge.apply_end_dt is '수가종료일';

comment on column tst_scm.bts_stnd_charge.insu_cd is '보험코드';

comment on column tst_scm.bts_stnd_charge.insu_cate_no is '보험분류번호';

comment on column tst_scm.bts_stnd_charge.relat_value_point is '상대가치점수';

comment on column tst_scm.bts_stnd_charge.insu_charge is '보험수가';

comment on column tst_scm.bts_stnd_charge.qlad_charge is '질가산료';

comment on column tst_scm.bts_stnd_charge.stnd_charge is '기준수가';

comment on column tst_scm.bts_stnd_charge.lowest_charge is '최저수가';

comment on column tst_scm.bts_stnd_charge.qlad_cd is '질가산코드';

comment on column tst_scm.bts_stnd_charge.relat_value_qlad_point is '상대가치질가산점수';

comment on column tst_scm.bts_stnd_charge.output_insu_cd is '출력보험코드';

comment on column tst_scm.bts_stnd_charge.total_qlad_charge is '질가산료합';

comment on column tst_scm.bts_stnd_charge.supval is '공급액';

comment on column tst_scm.bts_stnd_charge.addtax is '부가세액';

comment on column tst_scm.bts_stnd_charge.creator is '생성자';

comment on column tst_scm.bts_stnd_charge.create_dtime is '생성일시';

alter table tst_scm.bts_stnd_charge
    owner to ailis_user;

create table tst_scm.bbs_tst_ref_group
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

comment on table tst_scm.bbs_tst_ref_group is '검사 참조그룹';

comment on column tst_scm.bbs_tst_ref_group.ref_group_cd is '참조그룹 코드';

comment on column tst_scm.bbs_tst_ref_group.ref_nm is '참조그룹명';

comment on column tst_scm.bbs_tst_ref_group.ref_abbr_nm is '참조그룹명_약어';

comment on column tst_scm.bbs_tst_ref_group.ref_eng_nm is '참조그룹명(영문)';

comment on column tst_scm.bbs_tst_ref_group.ref_eng_abbr_nm is '참조그룹명_약어(영문)';

comment on column tst_scm.bbs_tst_ref_group.sort_order is '정렬순서';

comment on column tst_scm.bbs_tst_ref_group.creator is '생성자';

comment on column tst_scm.bbs_tst_ref_group.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_ref_group.updater is '수정자';

comment on column tst_scm.bbs_tst_ref_group.update_dtime is '수정일시';

alter table tst_scm.bbs_tst_ref_group
    owner to ailis_user;

create table tst_scm.bbs_tst_trn
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

comment on table tst_scm.bbs_tst_trn is '검사 트랜잭션';

comment on column tst_scm.bbs_tst_trn.tst_trn_id is 'UUID';

comment on column tst_scm.bbs_tst_trn.tst_req_dt is '의뢰일자';

comment on column tst_scm.bbs_tst_trn.tst_req_no is '의뢰번호';

comment on column tst_scm.bbs_tst_trn.tst_hst_cd is '이력테이블 ID (TRNID)';

comment on column tst_scm.bbs_tst_trn.hst_id is '이력 UUID';

comment on column tst_scm.bbs_tst_trn.creator is '생성자';

comment on column tst_scm.bbs_tst_trn.create_dtime is '생성일시';

alter table tst_scm.bbs_tst_trn
    owner to ailis_user;

create index bbs_tst_trn_idx01
    on tst_scm.bbs_tst_trn (tst_req_dt, tst_req_no, create_dtime, tst_hst_cd);

create table tst_scm.bbs_spcm
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
    coll_amt         varchar(100),
    eng_coll_amt     varchar(100)
        constraint ascii_only_eng_coll_amt
            check ((eng_coll_amt)::text ~ '^[\x20-\x7F]*$'::text),
    spcm_strg        varchar(100),
    eng_spcm_strg    varchar(100)
        constraint ascii_only_eng_spcm_strg
            check ((eng_spcm_strg)::text ~ '^[\x20-\x7F]*$'::text),
    spcm_safe        varchar(1000),
    eng_spcm_safe    varchar(1000),
    caution          varchar(1000),
    eng_caution      varchar(1000),
    ref              varchar(1000),
    eng_ref          varchar(1000),
    creator          varchar(50)  not null,
    create_dtime     timestamp    not null,
    updater          varchar(50)  not null,
    update_dtime     timestamp    not null
);

comment on table tst_scm.bbs_spcm is '검체';

comment on column tst_scm.bbs_spcm.spcm_cd is '검체코드';

comment on column tst_scm.bbs_spcm.spcm_cate_cd is '검체분류 (SPCM_CATE)';

comment on column tst_scm.bbs_spcm.use_yn is '사용여부';

comment on column tst_scm.bbs_spcm.spcm_nm is '검체명';

comment on column tst_scm.bbs_spcm.spcm_abbr_nm is '검체명_약어';

comment on column tst_scm.bbs_spcm.spcm_eng_nm is '검체명(영문)';

comment on column tst_scm.bbs_spcm.spcm_eng_abbr_nm is '검체명(영문)_약어';

comment on column tst_scm.bbs_spcm.coll_amt is '채취량';

comment on column tst_scm.bbs_spcm.eng_coll_amt is '채취량(영문)';

comment on column tst_scm.bbs_spcm.spcm_strg is '검체보관';

comment on column tst_scm.bbs_spcm.eng_spcm_strg is '검체보관(영문)';

comment on column tst_scm.bbs_spcm.spcm_safe is '검체안정성';

comment on column tst_scm.bbs_spcm.eng_spcm_safe is '검체안정성(영문)';

comment on column tst_scm.bbs_spcm.caution is '주의사항';

comment on column tst_scm.bbs_spcm.eng_caution is '주의사항(영문)';

comment on column tst_scm.bbs_spcm.ref is '참고사항';

comment on column tst_scm.bbs_spcm.eng_ref is '참고사항(영문)';

comment on column tst_scm.bbs_spcm.creator is '생성자';

comment on column tst_scm.bbs_spcm.create_dtime is '생성일시';

comment on column tst_scm.bbs_spcm.updater is '수정자';

comment on column tst_scm.bbs_spcm.update_dtime is '수정일시';

alter table tst_scm.bbs_spcm
    owner to ailis_user;

create table tst_scm.bbs_tst_cate
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

comment on table tst_scm.bbs_tst_cate is '검사분류';

comment on column tst_scm.bbs_tst_cate.tst_cate_id is 'UUID';

comment on column tst_scm.bbs_tst_cate.tst_large_cate_cd is '검사 대분류 코드';

comment on column tst_scm.bbs_tst_cate.tst_medium_cate_cd is '검사 중분류 코드';

comment on column tst_scm.bbs_tst_cate.cate_nm is '분류명';

comment on column tst_scm.bbs_tst_cate.cate_abbr_nm is '분류명_약어';

comment on column tst_scm.bbs_tst_cate.cate_eng_nm is '분류명_영문';

comment on column tst_scm.bbs_tst_cate.cate_eng_abbr_nm is '분류명_영문_약어';

comment on column tst_scm.bbs_tst_cate.use_yn is '사용여부';

comment on column tst_scm.bbs_tst_cate.sort_order is '정렬순서';

comment on column tst_scm.bbs_tst_cate.creator is '생성자';

comment on column tst_scm.bbs_tst_cate.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_cate.updater is '수정자';

comment on column tst_scm.bbs_tst_cate.update_dtime is '수정일시';

alter table tst_scm.bbs_tst_cate
    owner to ailis_user;

create table tst_scm.bbs_spcm_cntn
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

comment on table tst_scm.bbs_spcm_cntn is '검체 용기';

comment on column tst_scm.bbs_spcm_cntn.spcm_cntn_cd is '검체용기코드';

comment on column tst_scm.bbs_spcm_cntn.cntn_nm is '용기명';

comment on column tst_scm.bbs_spcm_cntn.cntn_eng_nm is '용기명(영문)';

comment on column tst_scm.bbs_spcm_cntn.cntn_file_id is '용기이미지';

comment on column tst_scm.bbs_spcm_cntn.creator is '생성자';

comment on column tst_scm.bbs_spcm_cntn.create_dtime is '생성일시';

comment on column tst_scm.bbs_spcm_cntn.updater is '수정자';

comment on column tst_scm.bbs_spcm_cntn.update_dtime is '수정일시';

alter table tst_scm.bbs_spcm_cntn
    owner to ailis_user;

create table tst_scm.bbs_tst_ref
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

comment on table tst_scm.bbs_tst_ref is '검사 참조항목';

comment on column tst_scm.bbs_tst_ref.ref_cd is '참조항목';

comment on column tst_scm.bbs_tst_ref.ref_cate_cd is '분류';

comment on column tst_scm.bbs_tst_ref.use_yn is '사용여부';

comment on column tst_scm.bbs_tst_ref.ref_nm is '참조명';

comment on column tst_scm.bbs_tst_ref.ref_abbr_nm is '참조명_약어';

comment on column tst_scm.bbs_tst_ref.ref_eng_nm is '참조명(영어)';

comment on column tst_scm.bbs_tst_ref.ref_eng_abbr_nm is '참조명_약어(영어)';

comment on column tst_scm.bbs_tst_ref.sort_order is '정렬순서';

comment on column tst_scm.bbs_tst_ref.ref_type is '데이터 타입';

comment on column tst_scm.bbs_tst_ref.ref_size is '데이터 크기';

comment on column tst_scm.bbs_tst_ref.range_chk_yn is '입력범위 확인';

comment on column tst_scm.bbs_tst_ref.ref_min_val is '최소값';

comment on column tst_scm.bbs_tst_ref.ref_max_val is '최대값';

comment on column tst_scm.bbs_tst_ref.data_format is '포멧';

comment on column tst_scm.bbs_tst_ref.dft_data is '기본값';

comment on column tst_scm.bbs_tst_ref.dft_eng_data is '기본값(영문)';

comment on column tst_scm.bbs_tst_ref.creator is '생성자';

comment on column tst_scm.bbs_tst_ref.create_dtime is '생성일시';

comment on column tst_scm.bbs_tst_ref.updater is '수정자';

comment on column tst_scm.bbs_tst_ref.update_dtime is '수정일시';

alter table tst_scm.bbs_tst_ref
    owner to ailis_user;

create table tst_scm.tbs_tst_report
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

comment on table tst_scm.tbs_tst_report is '검사결과 (보고서)';

comment on column tst_scm.tbs_tst_report.tst_report_id is 'UUID';

comment on column tst_scm.tbs_tst_report.tst_req_dt is '의뢰일자';

comment on column tst_scm.tbs_tst_report.tst_req_no is '의뢰번호';

comment on column tst_scm.tbs_tst_report.tst_cd is '검사코드';

comment on column tst_scm.tbs_tst_report.memo is '이력메모';

comment on column tst_scm.tbs_tst_report.lims_rcv_dtime is 'LIMS수신일시';

comment on column tst_scm.tbs_tst_report.rst_short is '결과 단문';

comment on column tst_scm.tbs_tst_report.rst_txt is '결과 장문 ';

comment on column tst_scm.tbs_tst_report.atch_grup_id is '결과파일 id';

comment on column tst_scm.tbs_tst_report.rst_file_nm is '결과파일 이름';

comment on column tst_scm.tbs_tst_report.rst_file_ext is '결과파일 종류';

comment on column tst_scm.tbs_tst_report.rst_file_path is '결과파일 경로';

comment on column tst_scm.tbs_tst_report.rst_url is '결과 URL';

comment on column tst_scm.tbs_tst_report.delivery_yn is '배포여부';

comment on column tst_scm.tbs_tst_report.delivery_cd is '배포방식';

comment on column tst_scm.tbs_tst_report.delivery_dtime is '배포일시';

comment on column tst_scm.tbs_tst_report.deliverer is '배포자';

comment on column tst_scm.tbs_tst_report.creator is '생성자';

comment on column tst_scm.tbs_tst_report.create_dtime is '생성일시';

comment on column tst_scm.tbs_tst_report.updater is '수정자';

comment on column tst_scm.tbs_tst_report.update_dtime is '수정일시';

alter table tst_scm.tbs_tst_report
    owner to ailis_user;

create table tst_scm.tbs_tst_report_hst
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

comment on table tst_scm.tbs_tst_report_hst is '검사결과 (보고서 이력)';

comment on column tst_scm.tbs_tst_report_hst.tst_report_hst_id is 'UUID';

comment on column tst_scm.tbs_tst_report_hst.hst_cd is '이력생성유형코드 HST';

comment on column tst_scm.tbs_tst_report_hst.hst_memo is '이력메모';

comment on column tst_scm.tbs_tst_report_hst.worker is '작업자';

comment on column tst_scm.tbs_tst_report_hst.work_dtime is '작업일시';

comment on column tst_scm.tbs_tst_report_hst.tst_req_dt is '의뢰일자';

comment on column tst_scm.tbs_tst_report_hst.tst_req_no is '의뢰번호';

comment on column tst_scm.tbs_tst_report_hst.tst_cd is '검사코드';

comment on column tst_scm.tbs_tst_report_hst.memo is '이력메모';

comment on column tst_scm.tbs_tst_report_hst.lims_rcv_dtime is 'LIMS수신일시';

comment on column tst_scm.tbs_tst_report_hst.rst_short is '결과 단문';

comment on column tst_scm.tbs_tst_report_hst.rst_txt is '결과 장문 ';

comment on column tst_scm.tbs_tst_report_hst.atch_grup_id is '결과파일 id';

comment on column tst_scm.tbs_tst_report_hst.rst_file_nm is '결과파일 이름';

comment on column tst_scm.tbs_tst_report_hst.rst_file_ext is '결과파일 종류';

comment on column tst_scm.tbs_tst_report_hst.rst_file_path is '결과파일 경로';

comment on column tst_scm.tbs_tst_report_hst.rst_url is '결과 URL';

comment on column tst_scm.tbs_tst_report_hst.delivery_yn is '배포여부';

comment on column tst_scm.tbs_tst_report_hst.delivery_cd is '배포방식';

comment on column tst_scm.tbs_tst_report_hst.delivery_dtime is '배포일시';

comment on column tst_scm.tbs_tst_report_hst.deliverer is '배포자';

comment on column tst_scm.tbs_tst_report_hst.creator is '생성자';

comment on column tst_scm.tbs_tst_report_hst.create_dtime is '생성일시';

comment on column tst_scm.tbs_tst_report_hst.updater is '수정자';

comment on column tst_scm.tbs_tst_report_hst.update_dtime is '수정일시';

alter table tst_scm.tbs_tst_report_hst
    owner to ailis_user;

create index tbs_tst_report_hst_idx01
    on tst_scm.tbs_tst_report_hst (tst_req_dt, tst_req_no, tst_cd, work_dtime);

create table tst_scm.bbs_gene
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

comment on table tst_scm.bbs_gene is '유전자';

comment on column tst_scm.bbs_gene.gene_cd is ' 유전자 코드';

comment on column tst_scm.bbs_gene.gene_nm is '유전자명';

comment on column tst_scm.bbs_gene.sort_order is '정렬순서';

comment on column tst_scm.bbs_gene.creator is '생성자';

comment on column tst_scm.bbs_gene.create_dtime is '생성일시';

comment on column tst_scm.bbs_gene.updater is '수정자';

comment on column tst_scm.bbs_gene.update_dtime is '수정일시';

alter table tst_scm.bbs_gene
    owner to ailis_user;

create index bbs_gene_idx01
    on tst_scm.bbs_gene (sort_order, gene_cd);

create index bbs_gene_idx02
    on tst_scm.bbs_gene (gene_nm);

create table tst_scm.bts_item
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
    creator            varchar(50)                        not null,
    create_dtime       timestamp                          not null,
    updater            varchar(50)                        not null,
    update_dtime       timestamp                          not null
);

comment on table tst_scm.bts_item is '검사종목';

comment on column tst_scm.bts_item.tst_cd is '검사코드';

comment on column tst_scm.bts_item.tst_large_cate_cd is '검사 대분류 코드';

comment on column tst_scm.bts_item.tst_medium_cate_cd is '검사 중분류 코드';

comment on column tst_scm.bts_item.start_dt is '시작일자';

comment on column tst_scm.bts_item.end_dt is '종료일자';

comment on column tst_scm.bts_item.use_yn is '사용여부';

comment on column tst_scm.bts_item.req_poss_yn is '의뢰가능여부';

comment on column tst_scm.bts_item.web_kor_yn is '한글 편람적용여부';

comment on column tst_scm.bts_item.web_eng_yn is '영문 편람적용여부';

comment on column tst_scm.bts_item.tst_nm is '검사명';

comment on column tst_scm.bts_item.tst_abbr_nm is '검사명_약어';

comment on column tst_scm.bts_item.tst_eng_nm is '검사명_영문';

comment on column tst_scm.bts_item.tst_eng_abbr_nm is '검사명_영문_약어';

comment on column tst_scm.bts_item.tst_int_nm is '검사명_내부';

comment on column tst_scm.bts_item.rst_type_short_yn is '결과형태 단문 ';

comment on column tst_scm.bts_item.rst_type_long_yn is '결과형태 장문 ';

comment on column tst_scm.bts_item.rst_type_file_yn is '결과형태 파일';

comment on column tst_scm.bts_item.rst_type_url_yn is '결과형태 URL';

comment on column tst_scm.bts_item.disease_cd is '질병코드';

comment on column tst_scm.bts_item.tst_method_cd is '검사방법코드';

comment on column tst_scm.bts_item.ref_val is '참고치';

comment on column tst_scm.bts_item.eng_ref_val is '참고치(영문)';

comment on column tst_scm.bts_item.clnc_sgnf is '임상적의의';

comment on column tst_scm.bts_item.eng_clnc_sgnf is '임상적의의(영문)';

comment on column tst_scm.bts_item.tst_desc is '검사설명';

comment on column tst_scm.bts_item.tst_eng_desc is '검사설명(영문)';

comment on column tst_scm.bts_item.tst_dayweek is '검사요일';

comment on column tst_scm.bts_item.tst_tatday is '검사소요일수';

comment on column tst_scm.bts_item.insu_apply_cd is '급여비급여구분';

comment on column tst_scm.bts_item.insu_cd is '보험코드';

comment on column tst_scm.bts_item.insu_cate_no is '보험분류번호';

comment on column tst_scm.bts_item.creator is '생성자';

comment on column tst_scm.bts_item.create_dtime is '생성일시';

comment on column tst_scm.bts_item.updater is '수정자';

comment on column tst_scm.bts_item.update_dtime is '수정일시';

alter table tst_scm.bts_item
    owner to ailis_user;

create index bts_item_idx01
    on tst_scm.bts_item (tst_large_cate_cd, tst_medium_cate_cd, tst_cd);

create table tst_scm.bts_item_hst
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
    creator            varchar(50)  not null,
    create_dtime       timestamp    not null,
    updater            varchar(50)  not null,
    update_dtime       timestamp    not null,
    constraint bts_item_hst_index01
        unique (tst_cd, item_hst_id)
);

comment on table tst_scm.bts_item_hst is '검사종목 (히스토리)';

comment on column tst_scm.bts_item_hst.item_hst_id is 'UUID';

comment on column tst_scm.bts_item_hst.hst_desc is '변경사유';

comment on column tst_scm.bts_item_hst.tst_cd is '검사코드';

comment on column tst_scm.bts_item_hst.tst_large_cate_cd is '검사 대분류 코드';

comment on column tst_scm.bts_item_hst.tst_medium_cate_cd is '검사 중분류 코드';

comment on column tst_scm.bts_item_hst.start_dt is '시작일자';

comment on column tst_scm.bts_item_hst.end_dt is '종료일자';

comment on column tst_scm.bts_item_hst.use_yn is '사용여부';

comment on column tst_scm.bts_item_hst.req_poss_yn is '의뢰가능여부';

comment on column tst_scm.bts_item_hst.web_kor_yn is '한글 편람적용여부';

comment on column tst_scm.bts_item_hst.web_eng_yn is '영문 편람적용여부';

comment on column tst_scm.bts_item_hst.tst_nm is '검사명';

comment on column tst_scm.bts_item_hst.tst_abbr_nm is '검사명_약어';

comment on column tst_scm.bts_item_hst.tst_eng_nm is '검사명_영문';

comment on column tst_scm.bts_item_hst.tst_eng_abbr_nm is '검사명_영문_약어';

comment on column tst_scm.bts_item_hst.tst_int_nm is '검사명_내부';

comment on column tst_scm.bts_item_hst.rst_type_short_yn is '결과형태 단문 ';

comment on column tst_scm.bts_item_hst.rst_type_long_yn is '결과형태 장문 ';

comment on column tst_scm.bts_item_hst.rst_type_file_yn is '결과형태 파일';

comment on column tst_scm.bts_item_hst.rst_type_url_yn is '결과형태 URL';

comment on column tst_scm.bts_item_hst.disease_cd is '질병코드';

comment on column tst_scm.bts_item_hst.tst_method_cd is '검사방법코드';

comment on column tst_scm.bts_item_hst.ref_val is '참고치';

comment on column tst_scm.bts_item_hst.eng_ref_val is '참고치(영문)';

comment on column tst_scm.bts_item_hst.clnc_sgnf is '임상적의의';

comment on column tst_scm.bts_item_hst.eng_clnc_sgnf is '임상적의의(영문)';

comment on column tst_scm.bts_item_hst.tst_desc is '검사설명';

comment on column tst_scm.bts_item_hst.tst_eng_desc is '검사설명(영문)';

comment on column tst_scm.bts_item_hst.tst_dayweek is '검사일';

comment on column tst_scm.bts_item_hst.tst_tatday is '검사소요일수';

comment on column tst_scm.bts_item_hst.insu_apply_cd is '급여비급여구분';

comment on column tst_scm.bts_item_hst.insu_cd is '보험코드';

comment on column tst_scm.bts_item_hst.insu_cate_no is '보험분류번호';

comment on column tst_scm.bts_item_hst.creator is '생성자';

comment on column tst_scm.bts_item_hst.create_dtime is '생성일시';

comment on column tst_scm.bts_item_hst.updater is '수정자';

comment on column tst_scm.bts_item_hst.update_dtime is '수정일시';

alter table tst_scm.bts_item_hst
    owner to ailis_user;

