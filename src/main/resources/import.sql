INSERT INTO `softplan`.`adjustment_factor`
(`id`,
`name`,
`jhi_condition`,
`condition_operator`,
`jhi_cost`)
VALUES
(1,
'Fator de ajuste para excedente à cinco toneladas',
'Acrescentar ao custo para cada tonelada excedente.',
'5',
0.02);


INSERT INTO `softplan`.`road_type`
(`id`,
`name`,
`jhi_cost`,
`unit`)
VALUES
(1,
'Pavimentada',
0.54,
'KM');

INSERT INTO `softplan`.`road_type`
(`id`,
`name`,
`jhi_cost`,
`unit`)
VALUES
(2,
'Não-pavimentada',
0.62,
'KM');


INSERT INTO `softplan`.`vehicle`
(`id`,
`name`,
`jhi_cost`)
VALUES
(1,
'Caminhão baú',
1.0);

INSERT INTO `softplan`.`vehicle`
(`id`,
`name`,
`jhi_cost`)
VALUES
(2,
'Caminhão caçamba',
1.05);


INSERT INTO `softplan`.`vehicle`
(`id`,
`name`,
`jhi_cost`)
VALUES
(3,
'Carreta',
1.12);

