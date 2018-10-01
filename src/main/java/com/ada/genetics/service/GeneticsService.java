package com.ada.genetics.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.ada.genetics.model.Counts;
import com.ada.genetics.model.GeneticMarker;
import com.ada.genetics.model.GeneticProfile;
import com.ada.genetics.model.ProfileKey;
import com.ada.genetics.repository.CountsRepository;
import com.ada.genetics.repository.GeneticsMarkerRepository;
import com.ada.genetics.repository.GeneticsProfileRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Service
public class GeneticsService {

	private final GeneticsProfileRepository geneticsProfileRepository;
	private final GeneticsMarkerRepository geneticsMarkerRepository;
	private final CountsRepository countsRepository;
	private final Logger logger;

	public GeneticsService(GeneticsProfileRepository geneticsProfileRepository,
			GeneticsMarkerRepository geneticsMarkerRepository, CountsRepository countsRepository, Logger logger) {
		this.geneticsMarkerRepository = geneticsMarkerRepository;
		this.geneticsProfileRepository = geneticsProfileRepository;
		this.countsRepository = countsRepository;
		this.logger = logger;
	}

	public void addGeneticProfile(String geneticProfileKey, String rawData) {
		logger.debug("Started addGeneticProfile with geneticProfileKey:" + geneticProfileKey + " rawData " + rawData);
		List<String> keys = getGeneticProfileKeys();
		if (keys.stream().noneMatch(k -> k.equalsIgnoreCase(geneticProfileKey))) {
			HashMap<String, Long> counts = new HashMap<String, Long>();
			geneticsProfileRepository.save(new ProfileKey(geneticProfileKey));
			List<GeneticMarker> list = new ArrayList<GeneticMarker>();
			try (Stream<String> stream = Files.lines(Paths.get(rawData)).skip(1)) {
				stream.forEach(line -> {
					String arr[] = line.split(" ");
					list.add(new GeneticMarker(geneticProfileKey, arr[0], arr[1], Integer.parseInt(arr[2]), arr[3]));
					if (!counts.containsKey(arr[3]))
						counts.put(arr[3], (long) 1);
					else
						counts.put(arr[3], counts.get(arr[3]) + 1);
				});
				geneticsMarkerRepository.save(ImmutableList.copyOf(list));
				counts.entrySet().stream().forEach(entry -> {
					String key = entry.getKey();
					long value = entry.getValue();
					countsRepository.save(new Counts(geneticProfileKey, key, value));
				});
				logger.debug("Saving genetic profile is completed");
			} catch (IOException e) {
				logger.error("caught IOException while trying to read the rawData file");
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				logger.error("caught IndexOutOfBoundsException while parsing the rawData");
			}
		} else {
			logger.error(geneticProfileKey + " already exists!");
		}
	}

	public List<String> getGeneticProfileKeys() {
		logger.debug("getting all genetic profile keys");
		List<String> keys = geneticsProfileRepository.findAll().stream().map(geneticProfile -> geneticProfile.getName())
				.collect(Collectors.toList());
		logger.debug("found " + keys.toString());
		return ImmutableList.copyOf(keys);
	}

	public long getGeneticProfileCount() {
		logger.debug("getting all genetic profile count");
		Long count = geneticsProfileRepository.count();
		logger.debug("count returned is " + count);
		return count;
	}

	public GeneticProfile getGeneticProfile(String geneticProfileKey) {
		List<String> keys = getGeneticProfileKeys();
		if (keys.stream().anyMatch(k -> k.equalsIgnoreCase(geneticProfileKey))) {
			logger.debug("Getting the genetic profile for " + geneticProfileKey);
			return new GeneticProfile(geneticProfileKey,
					ImmutableList.copyOf(geneticsMarkerRepository.findByProfileKey(geneticProfileKey)));
		} else {
			logger.error("Key not found");
			return null;
		}
	}

	public Map<String, Long> getGenotypeHistogram(String geneticProfileKey) {
		logger.debug("getting genotypeHistogram for " + geneticProfileKey);
		List<String> keys = getGeneticProfileKeys();
		if (keys.stream().anyMatch(k -> k.equalsIgnoreCase(geneticProfileKey))) {
			List<Counts> list = countsRepository.findByGeneticProfileKey(geneticProfileKey);
			Map<String, Long> ret = new HashMap<String, Long>();
			list.stream().forEach(count -> ret.put(count.getKey(), count.getValue()));
			return ImmutableMap.copyOf(ret);
		} else {
			logger.error("Key not found");
			return null;
		}
	}

	public Map<String, Long> getGlobalGenotypeHistogram() {
		logger.debug("getting global genotype histogram");
		List<String> keys = getGeneticProfileKeys();
		Map<String, Long> ret = new HashMap<String, Long>();
		keys.stream().forEach(key -> {
			List<Counts> count = countsRepository.findByGeneticProfileKey(key);
			count.forEach(genotype -> {
				String genotypeKey = genotype.getKey();
				Long genotypeValue = genotype.getValue();
				if (!ret.containsKey(genotypeKey))
					ret.put(genotypeKey, genotypeValue);
				else
					ret.put(genotypeKey, ret.get(genotypeKey) + genotypeValue);
			});
		});
		return ret;
	}

	public String getGenotype(String geneticProfileKey, String rsId) {
		List<String> keys = getGeneticProfileKeys();
		if (keys.stream().anyMatch(k -> k.equalsIgnoreCase(geneticProfileKey))) {
			logger.debug("Getting genotype for " + geneticProfileKey + " with rsId " + rsId);
			String genotype = geneticsMarkerRepository.findByProfileKeyAndRsid(geneticProfileKey, rsId).getGenotype();
			logger.debug("Got " + genotype);
			return genotype;
		} else {
			logger.debug("Key not found");
			return null;
		}
	}
}
