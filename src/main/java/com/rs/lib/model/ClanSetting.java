package com.rs.lib.model;

public enum ClanSetting {
/*0	    int 	2147483647*/ GAME_TIME(0),
/*1	    string	*/
/*2	    long	2147483647*/ FORUM_QFC(2),
/*3	    int 	2147483647*/
/*4	    int 	1*/			 IS_RECRUITING(4),
/*5	    int 	1*/			 HAS_TIMEZONE(5),
/*6	    int 	128*/		 HOME_WORLD(6),
/*7	    int 	1024*/		 NATIONAL_FLAG(7),
/*8	    int 	1*/
/*9	    int 	1*/
/*10	int 	1*/
/*11	int 	1*/
/*12	int 	1*/
/*13	int 	2147483647*/
/*14	int 	32768*/		 MOTIF_TOP_ICON(14),
/*15	int 	32768*/		 MOTIF_BOTTOM_ICON(15),
/*16	int 	2147483647*/ MOTIF_TOP_COLOR(16),
/*17	int 	2147483647*/ MOTIF_BOTTOM_COLOR(17),
/*18	int 	2147483647*/ MOTIF_PRIMARY_COLOR(18),
/*19	int 	2147483647*/ MOTIF_SECONDARY_COLOR(19),
/*20	int 	2147483647*/
/*21	int 	2147483647*/
/*22	int 	2147483647*/
/*23	int 	2147483647*/
/*24	int 	2147483647*/
/*25	int 	32*/
/*26	int 	512*/
/*27	int 	32*/
/*28	int 	512*/
/*29	int 	32*/
/*30	int 	512*/
/*31	int 	32*/
/*32	int 	512*/
/*33	int 	32*/
/*34	int 	512*/
/*35	int 	32*/
/*36	int 	512*/
/*37	int 	32*/
/*38	int 	512*/
/*39	int 	32*/
/*40	int 	512*/
/*41	int 	32*/
/*42	int 	512*/
/*43	int 	32*/
/*44	int 	512*/
/*45	int 	2147483647*/
/*46	int 	2147483647*/
/*47	int 	2147483647*/
/*48	int 	2147483647*/
/*49	int 	2147483647*/
/*50	int 	2147483647*/
/*51	int 	2147483647*/
/*52	int 	2147483647*/
/*53	int 	2147483647*/
/*54	int 	2147483647*/
/*55	int 	2147483647*/
/*56	int 	2147483647*/
/*57	int 	2147483647*/
/*58	int 	2147483647*/
/*59	int 	2147483647*/
/*60	int 	2147483647*/
/*61	int 	2147483647*/
/*62	int 	2147483647*/
/*63	int 	2147483647*/
/*64	int 	2147483647*/
/*65	int 	2147483647*/ EVENT0_OPEN_TO_RANK(65), //3716 enum
/*66	int 	2147483647*/ EVENT1_OPEN_TO_RANK(66),
/*67	int 	2147483647*/ EVENT2_OPEN_TO_RANK(67),
/*68	int 	2147483647*/ EVENT3_OPEN_TO_RANK(68),
/*69	int 	2147483647*/ EVENT4_OPEN_TO_RANK(69),
/*70	int 	2147483647*/ EVENT5_OPEN_TO_RANK(70),
/*71	int 	2147483647*/ EVENT6_OPEN_TO_RANK(71),
/*72	int 	2147483647*/ EVENT7_OPEN_TO_RANK(72),
/*73	int 	2147483647*/
/*74	int 	128*/   EVENT0_ACTIVITY_CATEGORY(74), //3687 -> 3689 enum
/*75	int 	128*/   EVENT1_ACTIVITY_CATEGORY(75),
/*76	int 	128*/   EVENT2_ACTIVITY_CATEGORY(76),
/*77	int 	128*/   EVENT3_ACTIVITY_CATEGORY(77),
/*78	int 	128*/   EVENT4_ACTIVITY_CATEGORY(78),
/*79	int 	128*/   EVENT5_ACTIVITY_CATEGORY(79),
/*80	int 	128*/   EVENT6_ACTIVITY_CATEGORY(80),
/*81	int 	128*/   EVENT7_ACTIVITY_CATEGORY(81),
/*82	int 	32768*/ EVENT0_LOCATION(82), //3696 enum for location
/*83	int 	32768*/ EVENT1_LOCATION(83),
/*84	int 	32768*/ EVENT2_LOCATION(84),
/*85	int 	32768*/ EVENT3_LOCATION(85),
/*86	int 	32768*/ EVENT4_LOCATION(86),
/*87	int 	32768*/ EVENT5_LOCATION(87),
/*88	int 	32768*/ EVENT6_LOCATION(88),
/*89	int 	32768*/ EVENT7_LOCATION(89),
/*90	int 	128*/   EVENT0_WORLD(90),
/*91	int 	128*/   EVENT1_WORLD(91),
/*92	int 	128*/   EVENT2_WORLD(92),
/*93	int 	128*/   EVENT3_WORLD(93),
/*94	int 	128*/   EVENT4_WORLD(94),
/*95	int 	128*/   EVENT5_WORLD(95),
/*96	int 	128*/   EVENT6_WORLD(96),
/*97	int 	128*/   EVENT7_WORLD(97),
/*98	int 	32768*/	EVENT0_RUNEDATE(98),
/*99	int 	32768*/ EVENT1_RUNEDATE(99),
/*100	int 	32768*/ EVENT2_RUNEDATE(100), 
/*101	int 	32768*/ EVENT3_RUNEDATE(101),
/*102	int 	32768*/ EVENT4_RUNEDATE(102),
/*103	int 	32768*/ EVENT5_RUNEDATE(103),
/*104	int 	32768*/ EVENT6_RUNEDATE(104),
/*105	int 	32768*/ EVENT7_RUNEDATE(105),
/*106	int 	2048*/  EVENT0_TIME(106), //3695 enum for hours
/*107	int 	2048*/  EVENT1_TIME(107),
/*108	int 	2048*/  EVENT2_TIME(108),
/*109	int 	2048*/  EVENT3_TIME(109),
/*110	int 	2048*/  EVENT4_TIME(110),
/*111	int 	2048*/  EVENT5_TIME(111),
/*112	int 	2048*/  EVENT6_TIME(112),
/*113	int 	2048*/  EVENT7_TIME(113),
/*114	int 	32768*/ EVENT0_ACTIVITY_SLOT(114), //3689 enum
/*115	int 	32768*/ EVENT1_ACTIVITY_SLOT(115),
/*116	int 	32768*/ EVENT2_ACTIVITY_SLOT(116),
/*117	int 	32768*/ EVENT3_ACTIVITY_SLOT(117),
/*118	int 	32768*/ EVENT4_ACTIVITY_SLOT(118),
/*119	int 	32768*/ EVENT5_ACTIVITY_SLOT(119),
/*120	int 	32768*/ EVENT6_ACTIVITY_SLOT(120),
/*121	int 	32768*/ EVENT7_ACTIVITY_SLOT(121),
/*122	long	2147483647*/ EVENT0_FORUM_QFC(122),
/*123	long	2147483647*/ EVENT1_FORUM_QFC(123),
/*124	long	2147483647*/ EVENT2_FORUM_QFC(124),
/*125	long	2147483647*/ EVENT3_FORUM_QFC(125),
/*126	long	2147483647*/ EVENT4_FORUM_QFC(126),
/*127	long	2147483647*/ EVENT5_FORUM_QFC(127),
/*128	long	2147483647*/ EVENT6_FORUM_QFC(128),
/*129	long	2147483647*/ EVENT7_FORUM_QFC(129),
/*130	int 	8*/		HIGHLIGHTED_EVENT(130),
/*131	int 	1*/
/*132	int 	1*/     EVENT0_ATTENDANCE_MANDATORY(132),
/*133	int 	1*/     EVENT1_ATTENDANCE_MANDATORY(133),
/*134	int 	1*/     EVENT2_ATTENDANCE_MANDATORY(134),
/*135	int 	1*/     EVENT3_ATTENDANCE_MANDATORY(135),
/*136	int 	1*/     EVENT4_ATTENDANCE_MANDATORY(136),
/*137	int 	1*/     EVENT5_ATTENDANCE_MANDATORY(137),
/*138	int 	1*/     EVENT6_ATTENDANCE_MANDATORY(138),
/*139	int 	1*/     EVENT7_ATTENDANCE_MANDATORY(139),
/*140	int 	1*/
/*141	int 	2147483647*/
/*142	int 	2147483647*/
/*143	int 	2147483647*/
/*144	int 	2147483647*/
/*145	int 	2147483647*/
/*146	int 	1*/
/*147	int 	1*/
/*148	int 	1*/
/*149	int 	1*/
/*150	int 	1*/
/*151	int 	1*/
/*152	int 	1*/
/*153	int 	1*/
/*154	int 	1*/
/*155	int 	1*/
/*156	int 	1*/
/*157	int 	1*/
/*158	int 	1*/
/*159	int 	1*/
/*160	int 	1*/
/*161	int 	1*/
/*162	int 	1*/
/*163	int 	1*/
/*164	int 	1*/
/*165	int 	1*/
/*166	int 	1*/
/*167	int 	1*/
/*168	int 	1*/
/*169	int 	1*/
/*170	int 	1*/
/*171	int 	1*/
/*172	int 	1*/
/*173	int 	1*/
/*174	int 	1*/
/*175	int 	1*/
/*176	int 	1*/
/*177	int 	1*/
/*178	int 	1*/
/*179	int 	1*/
/*180	int 	1*/
/*181	int 	1*/
/*182	int 	1*/
/*183	int 	1*/
/*184	int 	1*/
/*185	int 	1*/
/*186	int 	1*/
/*187	int 	1*/
/*188	int 	1*/
/*189	int 	1*/
/*190	int 	1*/
/*191	int 	1*/
/*192	int 	1*/
/*193	int 	1*/
/*194	int 	1*/
/*195	int 	1*/
/*196	int 	1*/
/*197	int 	1*/
/*198	int 	1*/
/*199	int 	1*/
/*200	int 	1*/
/*201	int 	1*/
/*202	int 	1*/
/*203	int 	1*/
/*204	int 	1*/
/*205	int 	1*/
/*206	int 	1*/
/*207	int 	1*/
/*208	int 	1*/
/*209	int 	1*/
/*210	int 	1*/
/*211	int 	1*/
/*212	int 	1*/
/*213	int 	1*/
/*214	int 	1*/
/*215	int 	1*/
/*216	int 	1*/
/*217	int 	1*/
/*218	int 	1*/
/*219	int 	1*/
/*220	int 	1*/
/*221	int 	1*/
/*222	int 	1*/
/*223	int 	1*/
/*224	int 	1*/
/*225	int 	1*/
/*226	int 	1*/
/*227	int 	1*/
/*228	int 	1*/
/*229	int 	1*/
/*230	int 	1*/
/*231	int 	1*/
/*232	int 	1*/
/*233	int 	1*/
/*234	int 	1*/
/*235	int 	1*/
/*236	int 	1*/
/*237	int 	1*/
/*238	int 	1*/
/*239	int 	1*/
/*240	int 	1*/
/*241	int 	1*/
/*242	int 	1*/
/*243	int 	1*/
/*244	int 	1*/
/*245	int 	1*/
/*246	int 	1*/
/*247	int 	1*/
/*248	int 	1*/
/*249	int 	1*/
/*250	int 	1*/
/*251	int 	1*/
/*252	int 	1*/
/*253	int 	1*/
/*254	int 	1*/
/*255	int 	1*/
/*256	int 	1*/
/*257	int 	1*/
/*258	int 	1*/
/*259	int 	1*/
/*260	int 	1*/
/*261	int 	1*/
/*262	int 	1*/
/*263	int 	1*/
/*264	int 	1*/
/*265	int 	1*/
/*266	int 	1*/
/*267	int 	1*/
/*268	int 	1*/
/*269	int 	1*/
/*270	int 	1*/
/*271	int 	1*/
/*272	int 	1*/
/*273	int 	1*/
/*274	int 	1*/
/*275	int 	1*/
/*276	int 	1*/
/*277	int 	1*/
/*278	int 	1*/
/*279	int 	1*/
/*280	int 	1*/
/*281	int 	1*/
/*282	int 	1*/
/*283	int 	1*/
/*284	int 	1*/
/*285	int 	1*/
/*286	int 	1*/
/*287	int 	1*/
/*288	int 	1*/
/*289	int 	1*/
/*290	int 	1*/
/*291	int 	1*/
/*292	int 	2*/
/*293	int 	1*/
/*294	int 	2147483647*/
/*295	int 	1*/
/*296	int 	1*/
/*297	int 	1*/
/*298	int 	1*/
/*299	int 	1*/
/*300	int 	1*/
/*301	int 	2147483647*/
/*302	int 	1*/
/*303	int 	1*/
/*304	int 	1*/
/*305	int 	1*/
/*306	int 	1*/
/*307	int 	4*/
/*308	int 	2147483647*/
/*309	int 	2147483647*/
/*310	int 	1*/
/*311	int 	1*/
/*312	int 	1*/
/*313	int 	1*/
/*314	int 	1*/
/*315	int 	1*/
/*316	int 	1*/
/*317	int 	1*/
/*318	int 	1*/
/*319	int 	1*/
/*320	int 	1*/
/*321	int 	1*/
/*322	int 	1*/
/*323	int 	1*/
/*324	int 	1*/
/*325	int 	1*/
/*326	int 	1*/
	;
	
	private int id;
	
	ClanSetting(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
